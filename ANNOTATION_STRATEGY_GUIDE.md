# Annotation-Based Strategy Pattern - Quick Start Guide

## 🚀 Super Simple Usage

Just like `@RestController` and `@RequestMapping`, you can now use `@StrategyHandler` and `@StrategyMapping` for automatic routing!

## Basic Example

### 1. Create a Handler with Annotations

```java
@StrategyHandler
public class PaymentProcessingHandler {

    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest request) {
        // Credit card logic
        return processedResult;
    }

    @StrategyMapping("PAYPAL")
    public PaymentResult handlePayPal(PaymentRequest request) {
        // PayPal logic
        return processedResult;
    }

    @StrategyMapping("CRYPTO")
    public PaymentResult handleCrypto(PaymentRequest request) {
        // Crypto logic
        return processedResult;
    }
}
```

### 2. Use in Controller

```java
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final StrategyDispatcher dispatcher;

    @PostMapping("/payment")
    public PaymentResult process(@RequestBody PaymentRequest request) {
        // That's it! One line - Spring handles everything else!
        return dispatcher.dispatch(request.getPaymentMethod(), PaymentResult.class, request);
    }
}
```

## ✨ That's It!

No interfaces to implement, no factories to create, no strategy classes to manage. Just annotate your methods and dispatch!

## Complete Working Example

### Affiliate Creation

```java
@StrategyHandler
public class AffiliateCreationHandler {

    @StrategyMapping("SUBSCRIBER")
    public Affiliate handleSubscriber(CreateAffiliateRequest request) {
        log.info("Creating subscriber affiliate");
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Subscriber-specific logic
        return affiliate;
    }

    @StrategyMapping("PROVIDER")
    public Affiliate handleProvider(CreateAffiliateRequest request) {
        log.info("Creating provider affiliate");
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Provider-specific logic
        return affiliate;
    }

    @StrategyMapping("WEALTH")
    public Affiliate handleWealth(CreateAffiliateRequest request) {
        log.info("Creating wealth affiliate");
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName());
        // Wealth-specific logic
        return affiliate;
    }
}
```

### Controller

```java
@RestController
@RequestMapping("/affiliate")
@RequiredArgsConstructor
public class AffiliateController {

    private final StrategyDispatcher dispatcher;

    @PostMapping("/create")
    public ResponseEntity<Affiliate> create(@RequestBody CreateAffiliateRequest request) {
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());
        Affiliate affiliate = dispatcher.dispatch(platformType, Affiliate.class, request);
        return ResponseEntity.ok(affiliate);
    }
}
```

## Comparison: Old vs New

### ❌ Old Way (Manual Strategy Pattern)

```java
// 1. Define interface
public interface PaymentStrategy extends Strategy<PaymentMethod> {
    PaymentResult processPayment(PaymentRequest request);
}

// 2. Create factory
@Component
public class PaymentStrategyFactory extends StrategyFactory<PaymentMethod, PaymentStrategy> {}

// 3. Implement each strategy as separate class
@Service
public class CreditCardStrategy implements PaymentStrategy { ... }

@Service
public class PayPalStrategy implements PaymentStrategy { ... }

// 4. Use in controller
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentStrategyFactory factory;

    @PostMapping("/payment")
    public PaymentResult process(@RequestBody PaymentRequest request) {
        PaymentMethod method = PaymentMethod.fromString(request.getPaymentMethod());
        PaymentStrategy strategy = factory.getStrategy(method);
        return strategy.processPayment(request);
    }
}
```

### ✅ New Way (Annotation-Based)

```java
// 1. Create handler with annotations
@StrategyHandler
public class PaymentProcessingHandler {

    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest request) { ... }

    @StrategyMapping("PAYPAL")
    public PaymentResult handlePayPal(PaymentRequest request) { ... }
}

// 2. Use in controller
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final StrategyDispatcher dispatcher;

    @PostMapping("/payment")
    public PaymentResult process(@RequestBody PaymentRequest request) {
        return dispatcher.dispatch(request.getPaymentMethod(), PaymentResult.class, request);
    }
}
```

## Advanced Features

### Multiple Keys per Method

```java
@StrategyMapping({"VISA", "MASTERCARD", "AMEX"})
public PaymentResult handleCreditCards(PaymentRequest request) {
    // All credit card types handled here
}
```

### Check if Handler Exists

```java
if (dispatcher.hasHandler(paymentMethod)) {
    return dispatcher.dispatch(paymentMethod, PaymentResult.class, request);
} else {
    return handleUnsupportedPayment();
}
```

### Organize by Domain

You can create multiple handler classes:

```java
@StrategyHandler
public class CreditCardPaymentHandler {
    @StrategyMapping("VISA")
    public PaymentResult handleVisa(PaymentRequest request) { ... }

    @StrategyMapping("MASTERCARD")
    public PaymentResult handleMastercard(PaymentRequest request) { ... }
}

@StrategyHandler
public class DigitalWalletHandler {
    @StrategyMapping("PAYPAL")
    public PaymentResult handlePayPal(PaymentRequest request) { ... }

    @StrategyMapping("APPLE_PAY")
    public PaymentResult handleApplePay(PaymentRequest request) { ... }
}
```

## Benefits

✅ **Zero Boilerplate** - No interfaces, factories, or separate strategy classes
✅ **Familiar Syntax** - Works like `@RestController` + `@RequestMapping`
✅ **Automatic Registration** - Spring handles everything
✅ **Clean Code** - All related logic in one place
✅ **Easy Testing** - Test methods directly
✅ **Type Safety** - Compile-time checking
✅ **Flexible Organization** - Group by domain or keep together

## How It Works Under the Hood

1. **`@StrategyHandler`** - Marks a class as containing strategy methods (like `@RestController`)
2. **`@StrategyMapping`** - Maps a method to a strategy key (like `@RequestMapping`)
3. **`StrategyMappingRegistry`** - Spring BeanPostProcessor that scans and registers mappings at startup
4. **`StrategyDispatcher`** - Routes requests to the correct method (like DispatcherServlet)

## When to Use What?

### Use Annotation-Based Approach When:
- ✅ Related strategies share similar logic
- ✅ You want all implementations visible in one place
- ✅ Strategies are simple to medium complexity
- ✅ You want minimal boilerplate

### Use Traditional Strategy Pattern When:
- ✅ Strategies are complex with many dependencies
- ✅ Each strategy needs separate unit tests
- ✅ You want strict separation of concerns
- ✅ Strategies might be in different modules

## Migration from Traditional Strategy Pattern

Already using the traditional approach? Keep it! Both work together:

```java
// Option 1: Traditional (still works)
PaymentStrategy strategy = factory.getStrategy(method);
PaymentResult result = strategy.processPayment(request);

// Option 2: Annotation-based (new way)
PaymentResult result = dispatcher.dispatch(method, PaymentResult.class, request);
```

You can gradually migrate or use both approaches in the same application!
