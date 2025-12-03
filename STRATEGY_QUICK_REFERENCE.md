# Strategy Pattern - Quick Reference Card

## 🎯 Choose Your Approach

### Annotation-Based (Simplest - Like @RestController)
**Perfect for most use cases!**

```java
// Handler
@StrategyHandler
public class PaymentHandler {
    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest req) { ... }

    @StrategyMapping("PAYPAL")
    public PaymentResult handlePayPal(PaymentRequest req) { ... }
}

// Controller
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final StrategyDispatcher dispatcher;

    @PostMapping("/pay")
    public PaymentResult pay(@RequestBody PaymentRequest req) {
        return dispatcher.dispatch(req.getMethod(), PaymentResult.class, req);
    }
}
```

**Lines of code: ~15** ✨

---

### Interface-Based (Like Feign - NEW!)
**Zero implementation code!**

```java
// Interface (NO implementation needed!)
@StrategyClient
public interface PaymentService {
    @StrategyMapping("CREDIT_CARD")
    PaymentResult processCreditCard(PaymentRequest req);

    @StrategyMapping("PAYPAL")
    PaymentResult processPayPal(PaymentRequest req);
}

// Config (one bean registration)
@Bean
public PaymentService paymentService(StrategyMappingRegistry registry) {
    return new StrategyClientFactoryBean<>(PaymentService.class, registry).getObject();
}

// Controller - call interface methods directly!
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay/credit-card")
    public PaymentResult payCreditCard(@RequestBody PaymentRequest req) {
        return paymentService.processCreditCard(req);
    }
}
```

**Lines of code: ~12** ✨✨---

### Traditional Strategy Pattern (Most Flexible)
**Use for complex strategies or separate modules**

```java
// Interface
public interface PaymentStrategy extends Strategy<PaymentMethod> {
    PaymentResult processPayment(PaymentRequest req);
## 📊 Comparison Matrix

| Feature | Interface-Based | Annotation-Based | Traditional |
|---------|----------------|-----------------|-------------|
| **Setup Complexity** | ⭐ Minimal | ⭐ Minimal | ⭐⭐⭐ Moderate |
| **Lines of Code** | ⭐⭐⭐ ~12 | ⭐⭐⭐ ~15 | ⭐⭐ ~40 |
| **Code Organization** | Interface only | All in one class | Separate classes |
| **Learning Curve** | ⭐ Easy (like Feign) | ⭐ Easy (like @RequestMapping) | ⭐⭐ Moderate |
| **Type Safety** | ⭐⭐⭐ Full | ⭐⭐⭐ Full | ⭐⭐⭐ Full |
| **Explicit Methods** | ⭐⭐⭐ Clear method names | ⭐⭐ Generic dispatch | ⭐⭐⭐ Clear methods |
| **Testability** | ⭐⭐⭐ Mock interface | ⭐⭐⭐ Test methods | ⭐⭐⭐ Test classes |
| **Refactoring** | ⭐⭐⭐ Easy | ⭐⭐⭐ Easy | ⭐⭐ Moderate |
| **Complex Logic** | ⭐⭐ Good for simple | ⭐⭐ Good for simple/medium | ⭐⭐⭐ Great for complex |
| **Spring Integration** | ⭐⭐⭐ Automatic | ⭐⭐⭐ Automatic | ⭐⭐⭐ Automatic |
| **Multi-Key Routing** | ⭐⭐⭐ Supported | ⭐⭐⭐ Supported | ⭐⭐⭐ Supported |
    public PaymentResult processPayment(PaymentRequest req) { ... }
}

// Controller
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentStrategyFactory factory;

    @PostMapping("/pay")
    public PaymentResult pay(@RequestBody PaymentRequest req) {
        PaymentStrategy strategy = factory.getStrategy(req.getMethod());
        return strategy.processPayment(req);
    }
}
```

**Lines of code: ~40**

---

## 📊 Comparison Matrix

| Feature | Annotation-Based | Traditional |
|---------|-----------------|-------------|
| **Setup Complexity** | ⭐ Minimal | ⭐⭐⭐ Moderate |
| **Lines of Code** | ⭐⭐⭐ ~15 | ⭐⭐ ~40 |
| **Code Organization** | All in one class | Separate classes |
| **Learning Curve** | ⭐ Easy (like @RequestMapping) | ⭐⭐ Moderate |
| **Type Safety** | ⭐⭐⭐ Full | ⭐⭐⭐ Full |
| **Testability** | ⭐⭐⭐ Test methods directly | ⭐⭐⭐ Test classes |
| **Refactoring** | ⭐⭐⭐ Easy | ⭐⭐ Moderate |
| **Complex Logic** | ⭐⭐ Good for simple/medium | ⭐⭐⭐ Great for complex |
| **Separation of Concerns** | ⭐⭐ Methods in one class | ⭐⭐⭐ Separate classes |
| **Spring Integration** | ⭐⭐⭐ Automatic | ⭐⭐⭐ Automatic |

---

## 🚀 Quick Start Templates

### Template 1: Annotation-Based (Recommended for Most Cases)

```java
// 1. Create handler
@StrategyHandler
public class MyHandler {
    @StrategyMapping("KEY_A")
    public Result handleA(Request req) { /* logic */ }

    @StrategyMapping("KEY_B")
    public Result handleB(Request req) { /* logic */ }
}

// 2. Use in controller
@RestController
@RequiredArgsConstructor
public class MyController {
    private final StrategyDispatcher dispatcher;

    @PostMapping("/endpoint")
    public Result handle(@RequestBody Request req) {
        return dispatcher.dispatch(req.getKey(), Result.class, req);
    }
}
```

### Template 2: Traditional Strategy Pattern

```java
// 1. Interface
public interface MyStrategy extends Strategy<MyKey> {
    Result execute(Request req);
}

// 2. Factory
@Component
public class MyStrategyFactory extends StrategyFactory<MyKey, MyStrategy> {}

// 3. Implementation
@Service
public class MyStrategyImpl implements MyStrategy {
    public MyKey getStrategyKey() { return MyKey.A; }
    public Result execute(Request req) { /* logic */ }
}

// 4. Controller
@RestController
@RequiredArgsConstructor
public class MyController {
    private final MyStrategyFactory factory;

    @PostMapping("/endpoint")
    public Result handle(@RequestBody Request req) {
        return factory.getStrategy(req.getKey()).execute(req);
    }
}
```

---

## 💡 Decision Guide

### Use Annotation-Based When:
- ✅ Getting started with strategy pattern
- ✅ Strategies are simple to medium complexity
- ✅ You want to see all implementations at a glance
- ✅ Rapid development is priority
- ✅ Similar to REST endpoint routing

### Use Traditional When:
- ✅ Strategies are complex with many dependencies
- ✅ Each strategy needs extensive unit testing
- ✅ Strategies might be in different modules/packages
- ✅ You need strict separation of concerns
- ✅ Team is already familiar with pattern

### Use Both When:
- ✅ Different parts of system have different needs
- ✅ Gradually migrating from one to the other
- ✅ Some strategies are simple, others complex

---

## 🎓 Real-World Examples

### Annotation-Based Examples
- ✅ Payment processing (credit card, PayPal, crypto)
- ✅ Notification delivery (email, SMS, push)
- ✅ Data export (CSV, JSON, XML)
- ✅ Content rendering (mobile, desktop, tablet)

### Traditional Pattern Examples
- ✅ Complex pricing engines with many rules
- ✅ ML model selection with initialization logic
- ✅ Multi-step workflow orchestration
- ✅ Plugin architecture with external modules

---

## ⚡ Performance

Both approaches have **identical runtime performance**:
- ✅ Strategy lookup: O(1) hash map
- ✅ No reflection overhead (cached at startup)
- ✅ Spring bean management (same for both)

---

## 🔧 Migration Path

Already using traditional? Add annotation-based gradually:

```java
// Existing: Traditional
PaymentResult result = factory.getStrategy(method).process(req);

// New: Annotation-based (works alongside)
PaymentResult result = dispatcher.dispatch(method, PaymentResult.class, req);
```

No breaking changes! Both work in the same application.

---
---

## 🔥 NEW: Composite Keys (Multi-Field Routing)

Route based on **multiple criteria** instead of just one!

```java
// Example: Route by Platform + Affiliate Type
@StrategyHandler
public class CompositeHandler {

    @StrategyMapping("PROVIDER:ORGANIZATION")
    public Affiliate handleProviderOrg(Request req) { ... }

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    public Affiliate handleProviderIndividual(Request req) { ... }

    @StrategyMapping("WEALTH:ORGANIZATION")
    public Affiliate handleWealthOrg(Request req) { ... }
}

// Usage
CompositeKey key = CompositeKey.of(platformType, affiliateType);
Affiliate result = dispatcher.dispatch(key, Affiliate.class, request);
```

**Use Cases:**
- Platform + Type: `PROVIDER:ORGANIZATION`, `WEALTH:INDIVIDUAL`
- Method + Region: `CREDIT_CARD:US`, `ALIPAY:CN`
- Tier + Feature: `PREMIUM:ADVANCED`, `FREE:BASIC`

---

## 📚 Documentation

- **Full Guide**: `STRATEGY_PATTERN_GUIDE.md`
- **Annotation Guide**: `ANNOTATION_STRATEGY_GUIDE.md`
- **Advanced Features**: `ADVANCED_STRATEGY_FEATURES.md` (Composite Keys, @StrategyClient)
- **This Reference**: `STRATEGY_QUICK_REFERENCE.md`md`
- **This Reference**: `STRATEGY_QUICK_REFERENCE.md`
