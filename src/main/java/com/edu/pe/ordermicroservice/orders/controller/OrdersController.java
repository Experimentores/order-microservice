package com.edu.pe.ordermicroservice.orders.controller;

import com.crudjpa.controller.CrudController;
import com.crudjpa.enums.MapFrom;
import com.edu.pe.ordermicroservice.orders.domain.model.Order;
import com.edu.pe.ordermicroservice.orders.domain.services.IOrderService;
import com.edu.pe.ordermicroservice.orders.enums.OrderStatus;
import com.edu.pe.ordermicroservice.orders.exception.InvalidCreateResourceException;
import com.edu.pe.ordermicroservice.orders.exception.ValidationException;
import com.edu.pe.ordermicroservice.orders.mapping.OrderMapper;
import com.edu.pe.ordermicroservice.orders.resources.CreateOrderResource;
import com.edu.pe.ordermicroservice.orders.resources.OrderResource;
import com.edu.pe.ordermicroservice.orders.resources.UpdateOrderResource;
import com.edu.pe.ordermicroservice.shoppingcart.client.IShoppingCartClient;
import com.edu.pe.ordermicroservice.shoppingcart.domain.model.ShoppingCart;
import com.edu.pe.ordermicroservice.users.client.IUserClient;
import com.edu.pe.ordermicroservice.users.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${tripstore.orders-service.path}")
public class OrdersController extends CrudController<Order, Long, OrderResource, CreateOrderResource, UpdateOrderResource> {

    private final IOrderService orderService;
    private final IShoppingCartClient shoppingCartClient;
    private final IUserClient userClient;

    public OrdersController(IOrderService orderService, OrderMapper mapper, IShoppingCartClient shoppingCartClient, IUserClient userClient) {
        super(orderService, mapper);
        this.orderService = orderService;
        this.shoppingCartClient = shoppingCartClient;
        this.userClient = userClient;
    }

    @Override
    protected boolean isValidCreateResource(CreateOrderResource createOrderResource) {
        return true;
    }

    @Override
    protected boolean isValidUpdateResource(UpdateOrderResource updateOrderResource) {
        return true;
    }

    @Override
    protected OrderResource fromModelToResource(Order order, MapFrom from) {
        OrderResource resource = mapper.fromModelToResource(order);
        resource.setUser(null);
        if(from != MapFrom.ANY) {
            if(from != MapFrom.CREATE) {
                // try get shopping cart and set to resource
                setShoppingCartToOrderResource(resource);
            }

            setUserToOrderResource(order.getUserId(), resource);
        }

        return resource;
    }

    @Override
    protected Order fromCreateResourceToModel(CreateOrderResource resource) {
        Order order = super.fromCreateResourceToModel(resource);
        order.setDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.IN_PROCESS.name());
        return order;
    }

    Optional<User> getUserFromId(Long id) {
        try {
            ResponseEntity<User> response = userClient.getUserById(id);
            if(response.getStatusCode() == HttpStatus.OK)
                return Optional.ofNullable(response.getBody());
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    Optional<ShoppingCart> getShoppingCartOfOrder(Long orderId) {
        try {
            ResponseEntity<ShoppingCart> response = shoppingCartClient.getShoppingCartByOrderId(orderId);
            if(response.getStatusCode() == HttpStatus.OK)
                return Optional.ofNullable(response.getBody());
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    void setShoppingCartToOrderResource(OrderResource resource) {
        Optional<ShoppingCart> shoppingCart = getShoppingCartOfOrder(resource.getId());
        shoppingCart.ifPresentOrElse(resource::setShoppingCart, () -> resource.setShoppingCart(null));
    }

    void setUserToOrderResource(Long userId, OrderResource resource) {
        Optional<User> user = getUserFromId(userId);
        user.ifPresentOrElse(resource::setUser, () -> resource.setUser(null));
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResource>> getAllOrders() {
        return getAll();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResource> getOrderById(@PathVariable Long id) {
        return getById(id);
    }


    private void validateBindingResult(BindingResult result) {
        if(result.hasErrors())
            throw new ValidationException(getErrorsFromResult(result));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResource> createOrder(@Valid @RequestBody CreateOrderResource resource, BindingResult result) {
        validateBindingResult(result);
        Optional<User> user = getUserFromId(resource.getUserId());
        if(user.isEmpty())
            throw new InvalidCreateResourceException("User with id %s doesnt exists or service is down.".formatted(resource.getUserId()));

        return insert(resource);
    }


    public Optional<ShoppingCart> deleteShoppingCartByOrderId(Long orderId) {
        try {
            ResponseEntity<ShoppingCart> response = shoppingCartClient.deleteShoppingCartByOrderId(orderId);
            if(response.getStatusCode() == HttpStatus.OK) {
                shoppingCartClient.deleteShoppingCartByOrderId(orderId);
                return Optional.ofNullable(response.getBody());
            }
        } catch (Exception ignored) {}

        return Optional.empty();
    }

    private void validateShoppingCartClientHealth() {
        validateHealthClient(shoppingCartClient, "ShoppingCart");
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResource> deleteOrderById(@PathVariable Long id) {
        // before erase, check if related entity service is up
        validateShoppingCartClientHealth();

        ResponseEntity<OrderResource> response = delete(id);
        if(response.getStatusCode() == HttpStatus.OK)
            deleteShoppingCartByOrderId(id);

        return response;
    }

    @DeleteMapping(value = "users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResource>> deleteOrdersByUserId(@PathVariable Long id) {
        validateShoppingCartClientHealth();

        List<Order> deleted = orderService.deleteOrdersByUserId(id);
        if(deleted.isEmpty())
            return ResponseEntity.noContent().build();


        List<OrderResource> resources = deleted.stream()
                .map(order -> {
                    OrderResource resource = mapper.fromModelToResource(order);

                    Optional<ShoppingCart> shoppingCart = deleteShoppingCartByOrderId(order.getId());
                    shoppingCart.ifPresent(resource::setShoppingCart);

                    return resource;
                })
                .toList();


        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResource>> getOrdersByUserId(@PathVariable Long id) {
        List<Order> orders = orderService.findOrdersByUserId(id);

        List<OrderResource> resources = orders.stream()
                .map(order -> {
                    OrderResource resource = mapper.fromModelToResource(order);
                    setShoppingCartToOrderResource(resource);
                    return resource;
                }).toList();

        if(resources.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(resources);
    }

    @RequestMapping(value = "healthcheck", method = RequestMethod.HEAD)
    ResponseEntity<Void> isOk() {
        validateShoppingCartClientHealth();
        return ResponseEntity.ok().build();
    }
}
