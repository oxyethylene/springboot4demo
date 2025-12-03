# Annotation-Based Strategy Pattern - Quick Start

## Ultra-Simple Usage

### Step 1: Define Your Service Interface

```java
@StrategyClient
public interface AffiliateCreationService {
    Affiliate create(CreateAffiliateRequest request);
    void validate(CreateAffiliateRequest request);
}
```

That's it! Just a normal interface with `@StrategyClient` annotation.

### Step 2: Create Implementations

```java
// Implementation for SUBSCRIBER platform
@StrategyMapping(key = "SUBSCRIBER")
public class SubscriberAffiliateService implements AffiliateCreationService {
    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        // Subscriber-specific logic
        return affiliate;
    }
}

// Implementation for PROVIDER platform
@StrategyMapping(key = "PROVIDER")
public class ProviderAffiliateService implements AffiliateCreationService {
    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        // Provider-specific logic
        return affiliate;
    }
}
```

Just implement the interface and mark with `@StrategyMapping` annotation!

### Step 3: Use It!

```java
@RestController
@RequiredArgsConstructor
public class AffiliateController {

    // Just inject the interface!
    private final AffiliateCreationService affiliateService;

    @PostMapping("/create")
    public Affiliate create(@RequestBody CreateAffiliateRequest request) {
        // Set routing context
        StrategyContext.setPlatform("SUBSCRIBER");

        try {
            // It automatically routes to the right implementation!
            return affiliateService.create(request);
        } finally {
            StrategyContext.clear();
        }
    }
}
```

## Multi-Dimensional Routing (Composite Keys)

Need routing by multiple criteria? Use composite keys!

```java
// Routes by PLATFORM + AFFILIATE_TYPE
@StrategyMapping(key = {"PROVIDER", "ORGANIZATION"})
public class ProviderOrgAffiliateService implements AffiliateCreationService {
    @Override
    public Affiliate create(CreateAffiliateRequest request) {
        // Provider + Organization specific logic
        return affiliate;
    }
}

// In controller:
StrategyContext.setPlatform("PROVIDER");
StrategyContext.setAffiliateType("ORGANIZATION");
// Routes to ProviderOrgAffiliateService!
```

## Complete Example

```java
// 1. Interface
@StrategyClient
public interface PaymentService {
    PaymentResult process(PaymentRequest request);
}

// 2. Implementations
@StrategyMapping(key = "CREDIT_CARD")
public class CreditCardPaymentService implements PaymentService {
    public PaymentResult process(PaymentRequest request) {
        // Credit card logic
    }
}

@StrategyMapping(key = "PAYPAL")
public class PayPalPaymentService implements PaymentService {
    public PaymentResult process(PaymentRequest request) {
        // PayPal logic
    }
}

// 3. Usage
@RequiredArgsConstructor
public class CheckoutController {
    private final PaymentService paymentService;

    public PaymentResult checkout(PaymentRequest request) {
        StrategyContext.set("paymentMethod", request.getPaymentMethod());
        try {
            return paymentService.process(request);
        } finally {
            StrategyContext.clear();
        }
    }
}
```

## Key Benefits

✅ **No Factory Classes** - Framework creates them automatically
✅ **No Manual Registration** - Strategies auto-discovered via annotations
✅ **Type-Safe** - Compile-time interface checking
✅ **Multi-Dimensional** - Support composite routing keys
✅ **Clean Code** - Just interfaces and implementations
✅ **Zero Boilerplate** - Framework handles all routing logic

## How It Works

1. **`@StrategyClient`** on interface → Framework creates dynamic proxy
2. **`@StrategyMapping`** on implementation → Framework registers it
3. **`StrategyContext`** sets routing keys → Framework routes to correct impl
4. **Call interface method** → Automatically invokes correct implementation

## Common Patterns

### Pattern 1: Single Dimension Routing
```java
@StrategyMapping(key = "SUBSCRIBER")  // Route by platform
```

### Pattern 2: Multi-Dimension Routing
```java
@StrategyMapping(key = {"PROVIDER", "ORGANIZATION"})  // Platform + Type
```

### Pattern 3: Fallback Strategy
```java
@StrategyMapping(key = "DEFAULT")  // Default implementation
```

## Setup

Add to your Spring Boot application:

```java
@Configuration
public class StrategyConfig {
    @Bean
    public StrategyMappingRegistry strategyMappingRegistry() {
        return new StrategyMappingRegistry();
    }

    @Bean
    public StrategyClientProcessor strategyClientProcessor(
            StrategyMappingRegistry registry) {
        return new StrategyClientProcessor(registry);
    }
}
```

## API Reference

### Annotations

- `@StrategyClient` - Mark interface as strategy client
- `@StrategyMapping(key = {...})` - Mark implementation with routing key(s)

### Context API

- `StrategyContext.setPlatform(String)` - Set platform routing key
- `StrategyContext.setAffiliateType(String)` - Set affiliate type key
- `StrategyContext.set(String, Object)` - Set custom routing key
- `StrategyContext.clear()` - Clear context (call in finally block!)

### Composite Keys

- `CompositeKey.of(String...)` - Create composite key
- `CompositeKey.of(Object...)` - Create from objects (uses toString)

## Migration from Old Approach

**Before:**
```java
PlatformType platform = PlatformType.fromId(request.getPlatformId());
AffiliateCreationStrategy strategy = factory.getStrategy(platform);
Affiliate affiliate = strategy.createAffiliate(request);
```

**After:**
```java
StrategyContext.setPlatform(platform.name());
try {
    return affiliateService.create(request);  // Just call the service!
} finally {
    StrategyContext.clear();
}
```

## FAQ

**Q: Do I need to create a factory class?**
A: No! The framework creates it automatically.

**Q: How do I add a new implementation?**
A: Just create a class with `@StrategyMapping`, that's it!

**Q: Can I use multiple routing keys?**
A: Yes! Use `@StrategyMapping(key = {"KEY1", "KEY2"})` for composite routing.

**Q: What if no strategy is found?**
A: `IllegalArgumentException` is thrown with available keys listed.

**Q: Can I have multiple strategy types in one app?**
A: Yes! Each `@StrategyClient` interface is independent.
