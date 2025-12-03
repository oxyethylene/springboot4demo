# Generic Strategy Pattern Framework

## Overview

This framework provides a reusable, type-safe implementation of the Strategy Pattern for Spring Boot applications. It eliminates boilerplate code and allows you to quickly implement routing logic for different implementations based on a key.

## Core Components

### 1. `Strategy<K>` Interface
Generic interface that all strategies must implement.

```java
public interface Strategy<K> {
    K getStrategyKey();
}
```

### 2. `StrategyFactory<K, S>` Abstract Class
Base factory class that auto-discovers and registers strategies via Spring's dependency injection.

```java
public abstract class StrategyFactory<K, S extends Strategy<K>> {
    public S getStrategy(K key) { ... }
    public boolean hasStrategy(K key) { ... }
}
```

### 3. `StrategyRouter<K, S>` Component (Alternative)
Standalone router component for cases where inheritance is not desired.

## Usage Examples

### Example 1: Affiliate Creation by Platform

**Step 1: Define your key enum**
```java
public enum PlatformType {
    SUBSCRIBER, PROVIDER, WEALTH
}
```

**Step 2: Create strategy interface**
```java
public interface AffiliateCreationStrategy extends Strategy<PlatformType> {
    @Override
    default PlatformType getStrategyKey() {
        return getPlatformType();
    }

    PlatformType getPlatformType();
    Affiliate createAffiliate(CreateAffiliateRequest request);
}
```

**Step 3: Create concrete implementations**
```java
@Service
public class SubscriberAffiliateCreationStrategy implements AffiliateCreationStrategy {
    @Override
    public PlatformType getPlatformType() {
        return PlatformType.SUBSCRIBER;
    }

    @Override
    public Affiliate createAffiliate(CreateAffiliateRequest request) {
        // Subscriber-specific logic
    }
}
```

**Step 4: Create factory (one line!)**
```java
@Component
public class AffiliateCreationStrategyFactory
    extends StrategyFactory<PlatformType, AffiliateCreationStrategy> {
}
```

**Step 5: Use in controller**
```java
@RestController
@RequiredArgsConstructor
public class AffiliateController {
    private final AffiliateCreationStrategyFactory strategyFactory;

    @PostMapping("/create")
    public Affiliate createAffiliate(@RequestBody CreateAffiliateRequest request) {
        PlatformType platformType = PlatformType.fromId(request.getPlatformId());
        AffiliateCreationStrategy strategy = strategyFactory.getStrategy(platformType);
        return strategy.createAffiliate(request);
    }
}
```

### Example 2: Payment Processing by Method

**Strategy interface**
```java
public interface PaymentProcessingStrategy extends Strategy<PaymentMethod> {
    @Override
    default PaymentMethod getStrategyKey() {
        return getPaymentMethod();
    }

    PaymentMethod getPaymentMethod();
    PaymentResult processPayment(PaymentRequest request);
}
```

**Factory (one line!)**
```java
@Component
public class PaymentProcessingStrategyFactory
    extends StrategyFactory<PaymentMethod, PaymentProcessingStrategy> {
}
```

**Implementation**
```java
@Service
public class CreditCardPaymentStrategy implements PaymentProcessingStrategy {
    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CREDIT_CARD;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // Credit card specific logic
    }
}
```

## Benefits

✅ **Type-Safe**: Compile-time checking via generics
✅ **Auto-Registration**: Spring automatically discovers all strategy implementations
✅ **Zero Boilerplate**: Factory is just one line of code
✅ **Flexible Keys**: Use enums, strings, or any object as routing key
✅ **Easy Testing**: Mock individual strategies
✅ **Open/Closed Principle**: Add new strategies without modifying existing code
✅ **Single Responsibility**: Each strategy handles one implementation

## Quick Start Template

For any new routing scenario:

```java
// 1. Define your strategy interface
public interface YourStrategy extends Strategy<YourKeyType> {
    @Override
    default YourKeyType getStrategyKey() { return getKey(); }

    YourKeyType getKey();
    YourResult executeLogic(YourRequest request);
}

// 2. Create factory (one line!)
@Component
public class YourStrategyFactory
    extends StrategyFactory<YourKeyType, YourStrategy> {
}

// 3. Implement strategies
@Service
public class ConcreteStrategyA implements YourStrategy {
    @Override
    public YourKeyType getKey() { return YourKeyType.A; }

    @Override
    public YourResult executeLogic(YourRequest request) {
        // Implementation A
    }
}

// 4. Use in your code
YourStrategy strategy = yourStrategyFactory.getStrategy(key);
YourResult result = strategy.executeLogic(request);
```

## Common Use Cases

- **Payment Processing**: Route by payment method (credit card, PayPal, crypto)
- **Notification Delivery**: Route by channel (email, SMS, push, webhook)
- **Data Export**: Route by format (CSV, JSON, XML, PDF)
- **Authentication**: Route by auth type (OAuth, JWT, API key, SAML)
- **File Processing**: Route by file type (image, video, document)
- **Pricing Calculation**: Route by customer tier (free, premium, enterprise)
- **Content Rendering**: Route by device type (mobile, desktop, tablet)
- **API Versioning**: Route by API version (v1, v2, v3)

## Advanced Features

### Check if strategy exists
```java
if (strategyFactory.hasStrategy(key)) {
    // Handle strategy
} else {
    // Handle missing strategy
}
```

### Handle missing strategies gracefully
The framework throws `IllegalArgumentException` by default. Override for custom handling:

```java
@Component
public class CustomStrategyFactory extends StrategyFactory<Key, Strategy> {
    @Override
    public Strategy getStrategy(Key key) {
        if (!hasStrategy(key)) {
            // Custom logic: logging, fallback, etc.
            return getFallbackStrategy();
        }
        return super.getStrategy(key);
    }
}
```

## Architecture

```
┌─────────────────────────────────────────┐
│         Strategy<K> Interface           │
│  ┌───────────────────────────────────┐  │
│  │  K getStrategyKey()               │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
                    ▲
                    │ implements
    ┌───────────────┴───────────────┐
    │                               │
┌───┴────────────────┐   ┌──────────┴──────────┐
│ YourStrategy A     │   │ YourStrategy B      │
│ (Spring @Service)  │   │ (Spring @Service)   │
└────────────────────┘   └─────────────────────┘
                    │
                    │ auto-discovered by
                    ▼
┌─────────────────────────────────────────┐
│  StrategyFactory<K, S>                  │
│  ┌───────────────────────────────────┐  │
│  │ S getStrategy(K key)              │  │
│  │ boolean hasStrategy(K key)        │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
                    ▲
                    │ extends
         ┌──────────┴──────────┐
         │ YourStrategyFactory │
         │  (Spring @Component)│
         └─────────────────────┘
```

## Migration from Existing Code

If you have existing strategy pattern implementations:

1. Make your strategy interface extend `Strategy<K>`
2. Replace your factory class with: `extends StrategyFactory<K, S>`
3. Remove all factory boilerplate code (constructor, map, registration logic)
4. Done! The framework handles the rest.

## Testing

```java
@Test
public void testStrategyRouting() {
    // Mock strategies
    YourStrategy mockStrategy = mock(YourStrategy.class);
    when(mockStrategy.getStrategyKey()).thenReturn(YourKeyType.A);

    // Test factory
    YourStrategyFactory factory = new YourStrategyFactory();
    // ... set up factory with mock

    YourStrategy result = factory.getStrategy(YourKeyType.A);
    assertEquals(mockStrategy, result);
}
```
