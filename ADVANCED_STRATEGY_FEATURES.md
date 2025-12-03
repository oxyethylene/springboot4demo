# Advanced Features Guide

## 1. Composite Keys (Multi-Field Routing)

### Problem
Sometimes you need to route based on **multiple fields**, not just one.

**Example**: Creating affiliates based on:
- Platform (PROVIDER, SUBSCRIBER, WEALTH)
- Affiliate Type (ORGANIZATION, INDIVIDUAL)

This creates combinations like:
- `PROVIDER:ORGANIZATION` (requires corporate verification)
- `PROVIDER:INDIVIDUAL` (requires personal KYC)
- `WEALTH:ORGANIZATION` (requires financial institution checks)
- `WEALTH:INDIVIDUAL` (requires high net worth verification)

### Solution: CompositeKey

```java
// Create composite key from multiple fields
CompositeKey key = CompositeKey.of(platformType, affiliateType);

// Dispatch with composite key
Affiliate affiliate = dispatcher.dispatch(key, Affiliate.class, request);
```

### Implementation

**Step 1: Create Handler with Composite Keys**

```java
@StrategyHandler
public class CompositeAffiliateHandler {

    // Format: "FIELD1:FIELD2:FIELD3..."
    @StrategyMapping("PROVIDER:ORGANIZATION")
    public Affiliate handleProviderOrg(CreateAffiliateRequest request) {
        // Provider + Organization specific logic
        // - Corporate verification
        // - Tax ID validation
        return affiliate;
    }

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    public Affiliate handleProviderIndividual(CreateAffiliateRequest request) {
        // Provider + Individual specific logic
        // - Personal KYC
        return affiliate;
    }

    @StrategyMapping("WEALTH:ORGANIZATION")
    public Affiliate handleWealthOrg(CreateAffiliateRequest request) {
        // Wealth + Organization specific logic
        return affiliate;
    }
}
```

**Step 2: Use in Controller**

```java
@RestController
@RequiredArgsConstructor
public class AffiliateController {
    private final StrategyDispatcher dispatcher;

    @PostMapping("/affiliate")
    public Affiliate create(
            @RequestBody CreateAffiliateRequest request,
            @RequestParam String affiliateType) {

        PlatformType platform = PlatformType.fromId(request.getPlatformId());

        // Build composite key
        CompositeKey key = CompositeKey.of(platform, affiliateType);

        // Dispatch - routes to "PROVIDER:ORGANIZATION", etc.
        return dispatcher.dispatch(key, Affiliate.class, request);
    }
}
```

### Composite Key Features

```java
// Create from multiple parts
CompositeKey key = CompositeKey.of("PROVIDER", "ORGANIZATION", "US");
// Result: "PROVIDER:ORGANIZATION:US"

// Parse from string
CompositeKey key = CompositeKey.parse("WEALTH:INDIVIDUAL");

// Access individual parts
String platform = key.getPart(0);  // "WEALTH"
String type = key.getPart(1);      // "INDIVIDUAL"

// Get size
int size = key.size();  // 2

// Automatically converts to string for routing
String routing = key.toString();  // "WEALTH:INDIVIDUAL"
```

### Use Cases for Composite Keys

1. **Platform + Type**: `PROVIDER:ORGANIZATION`, `WEALTH:INDIVIDUAL`
2. **Region + Currency**: `US:USD`, `EU:EUR`, `ASIA:JPY`
3. **Tier + Feature**: `PREMIUM:ADVANCED`, `FREE:BASIC`
4. **Department + Role**: `SALES:MANAGER`, `ENGINEERING:LEAD`
5. **Channel + Priority**: `EMAIL:HIGH`, `SMS:URGENT`

---

## 2. Interface-Based Strategy Clients (@StrategyClient)

### Problem
With `@StrategyHandler`, you still need to:
1. Create handler classes
2. Write implementation methods
3. Call via dispatcher

Can we make it even simpler, like **Feign** or **@HttpExchange**?

### Solution: @StrategyClient

**Define an interface - NO IMPLEMENTATION NEEDED!**

```java
@StrategyClient
public interface AffiliateCreationService {

    @StrategyMapping("SUBSCRIBER")
    Affiliate createSubscriberAffiliate(CreateAffiliateRequest request);

    @StrategyMapping("PROVIDER")
    Affiliate createProviderAffiliate(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH")
    Affiliate createWealthAffiliate(CreateAffiliateRequest request);
}
```

**Register the interface:**

```java
@Configuration
public class StrategyClientConfig {

    @Bean
    public AffiliateCreationService affiliateCreationService(
            StrategyMappingRegistry registry) throws Exception {
        return new StrategyClientFactoryBean<>(
            AffiliateCreationService.class, registry
        ).getObject();
    }
}
```

**Use like any Spring bean:**

```java
@RestController
@RequiredArgsConstructor
public class AffiliateController {

    private final AffiliateCreationService affiliateService;

    @PostMapping("/subscriber")
    public Affiliate createSubscriber(@RequestBody CreateAffiliateRequest req) {
        // Call interface method - Spring creates implementation automatically!
        return affiliateService.createSubscriberAffiliate(req);
    }

    @PostMapping("/provider")
    public Affiliate createProvider(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createProviderAffiliate(req);
    }
}
```

### How It Works

1. **You define** the interface with `@StrategyClient`
2. **Spring creates** a dynamic proxy at runtime
3. **Proxy routes** method calls to the correct `@StrategyHandler` method
4. **No implementation** code needed!

### Composite Keys with @StrategyClient

```java
@StrategyClient
public interface AffiliateCreationService {

    // Single key
    @StrategyMapping("PROVIDER")
    Affiliate createProvider(CreateAffiliateRequest request);

    // Composite keys
    @StrategyMapping("PROVIDER:ORGANIZATION")
    Affiliate createProviderOrganization(CreateAffiliateRequest request);

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    Affiliate createProviderIndividual(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH:ORGANIZATION")
    Affiliate createWealthOrganization(CreateAffiliateRequest request);
}
```

### Benefits of @StrategyClient

✅ **No Handler Classes** - Just define interface
✅ **Type-Safe** - Compile-time method signatures
✅ **Self-Documenting** - Clear method names
✅ **Testable** - Easy to mock interfaces
✅ **Familiar Pattern** - Like Feign, @HttpExchange
✅ **Works with Composite Keys** - Full feature support

---

## 3. Comparison: All Approaches

### Approach 1: Traditional Strategy Pattern

```java
// Interface
public interface PaymentStrategy extends Strategy<PaymentMethod> {
    PaymentResult process(PaymentRequest req);
}

// Factory
@Component
public class PaymentStrategyFactory
    extends StrategyFactory<PaymentMethod, PaymentStrategy> {}

// Implementation
@Service
public class CreditCardStrategy implements PaymentStrategy {
    public PaymentMethod getStrategyKey() { return CREDIT_CARD; }
    public PaymentResult process(PaymentRequest req) { /* impl */ }
}

// Usage
PaymentStrategy strategy = factory.getStrategy(method);
return strategy.process(request);
```

**Lines of code**: ~50
**Flexibility**: ⭐⭐⭐ High
**Simplicity**: ⭐⭐ Medium
**Best for**: Complex strategies, separate modules

---

### Approach 2: Annotation-Based (@StrategyHandler)

```java
// Handler
@StrategyHandler
public class PaymentHandler {
    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest req) { /* impl */ }

    @StrategyMapping("PAYPAL")
    public PaymentResult handlePayPal(PaymentRequest req) { /* impl */ }
}

// Usage
return dispatcher.dispatch(method, PaymentResult.class, request);
```

**Lines of code**: ~20
**Flexibility**: ⭐⭐⭐ High
**Simplicity**: ⭐⭐⭐ High
**Best for**: Most use cases

---

### Approach 3: Interface-Based (@StrategyClient)

```java
// Interface (no implementation!)
@StrategyClient
public interface PaymentService {
    @StrategyMapping("CREDIT_CARD")
    PaymentResult processCreditCard(PaymentRequest req);

    @StrategyMapping("PAYPAL")
    PaymentResult processPayPal(PaymentRequest req);
}

// Config
@Bean
public PaymentService paymentService(StrategyMappingRegistry registry) {
    return new StrategyClientFactoryBean<>(PaymentService.class, registry).getObject();
}

// Usage
return paymentService.processCreditCard(request);
```

**Lines of code**: ~15
**Flexibility**: ⭐⭐ Medium
**Simplicity**: ⭐⭐⭐ Highest
**Best for**: Simple routing, explicit methods

---

## 4. Complete Examples

### Example 1: Payment Processing with Composite Keys

Route by Payment Method + Payment Region:

```java
// Handler
@StrategyHandler
public class PaymentHandler {

    @StrategyMapping("CREDIT_CARD:US")
    public PaymentResult handleUSCreditCard(PaymentRequest req) {
        // US-specific credit card processing (Stripe, etc.)
    }

    @StrategyMapping("CREDIT_CARD:EU")
    public PaymentResult handleEUCreditCard(PaymentRequest req) {
        // EU-specific (PSD2, SCA requirements)
    }

    @StrategyMapping("CREDIT_CARD:ASIA")
    public PaymentResult handleAsiaCreditCard(PaymentRequest req) {
        // Asia-specific processors
    }

    @StrategyMapping("ALIPAY:CN")
    public PaymentResult handleAlipay(PaymentRequest req) {
        // Alipay processing
    }
}

// Usage
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final StrategyDispatcher dispatcher;

    @PostMapping("/pay")
    public PaymentResult pay(@RequestBody PaymentRequest req) {
        CompositeKey key = CompositeKey.of(req.getMethod(), req.getRegion());
        return dispatcher.dispatch(key, PaymentResult.class, req);
    }
}
```

### Example 2: Notification System with Interface

```java
// Interface
@StrategyClient
public interface NotificationService {

    @StrategyMapping("EMAIL")
    void sendEmail(String recipient, String message);

    @StrategyMapping("SMS")
    void sendSms(String recipient, String message);

    @StrategyMapping("PUSH")
    void sendPush(String recipient, String message);

    @StrategyMapping("SLACK")
    void sendSlack(String recipient, String message);
}

// Handler (provides actual implementation)
@StrategyHandler
public class NotificationHandler {

    @StrategyMapping("EMAIL")
    public void sendEmail(String recipient, String message) {
        // SMTP logic
    }

    @StrategyMapping("SMS")
    public void sendSms(String recipient, String message) {
        // SMS gateway logic
    }

    @StrategyMapping("PUSH")
    public void sendPush(String recipient, String message) {
        // FCM/APNS logic
    }

    @StrategyMapping("SLACK")
    public void sendSlack(String recipient, String message) {
        // Slack API logic
    }
}

// Usage
@Service
@RequiredArgsConstructor
public class UserService {
    private final NotificationService notificationService;

    public void notifyUser(User user, String message) {
        // Type-safe, clean interface
        notificationService.sendEmail(user.getEmail(), message);
    }
}
```

---

## 5. Best Practices

### When to Use Composite Keys

✅ **DO use** when routing depends on multiple independent factors
✅ **DO use** for cross-cutting concerns (region, tier, feature)
✅ **DO keep** composite keys to 2-3 parts max
❌ **DON'T use** for hierarchical relationships (use inheritance)
❌ **DON'T use** when a single key suffices

### When to Use @StrategyClient

✅ **DO use** for simple, well-defined APIs
✅ **DO use** when you want explicit method names
✅ **DO use** for public service interfaces
❌ **DON'T use** for complex conditional logic
❌ **DON'T use** when strategies need state/dependencies

### Mixing Approaches

You can use different approaches in the same application!

```java
// Public API: Interface-based (clean, explicit)
@StrategyClient
public interface PublicPaymentService {
    @StrategyMapping("CREDIT_CARD")
    PaymentResult processCreditCard(PaymentRequest req);
}

// Internal: Annotation-based (flexible, complex logic)
@StrategyHandler
public class InternalPaymentHandler {
    @StrategyMapping("CREDIT_CARD:US:PREMIUM")
    public PaymentResult handlePremiumUSPayment(PaymentRequest req) {
        // Complex internal logic with dependencies
    }
}
```

---

## 6. Migration Guide

### From Traditional to Annotation-Based

```java
// Before: Traditional
@Service
public class CreditCardStrategy implements PaymentStrategy {
    public PaymentMethod getStrategyKey() { return CREDIT_CARD; }
    public PaymentResult process(PaymentRequest req) { /* impl */ }
}

// After: Annotation-based
@StrategyHandler
public class PaymentHandler {
    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest req) { /* impl */ }
}
```

### From Annotation to Interface-Based

```java
// Before: Annotation
@StrategyHandler
public class PaymentHandler {
    @StrategyMapping("CREDIT_CARD")
    public PaymentResult handleCreditCard(PaymentRequest req) { /* impl */ }
}
return dispatcher.dispatch(method, PaymentResult.class, req);

// After: Interface
@StrategyClient
public interface PaymentService {
    @StrategyMapping("CREDIT_CARD")
    PaymentResult processCreditCard(PaymentRequest req);
}
return paymentService.processCreditCard(req);

// Keep the handler - it provides the implementation!
```

---

## 7. Testing

### Test @StrategyClient

```java
@Test
void testPaymentService() {
    // Mock the underlying handler
    when(paymentHandler.handleCreditCard(any()))
        .thenReturn(successResult);

    // Test interface method
    PaymentResult result = paymentService.processCreditCard(request);

    assertThat(result).isEqualTo(successResult);
}
```

### Test Composite Keys

```java
@Test
void testCompositeKeyRouting() {
    CompositeKey key = CompositeKey.of("PROVIDER", "ORGANIZATION");

    Affiliate result = dispatcher.dispatch(key, Affiliate.class, request);

    verify(handler).handleProviderOrganization(request);
}
```
