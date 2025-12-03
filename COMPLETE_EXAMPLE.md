# Complete Working Example - All Features

## Scenario: Affiliate Creation System

You need to create affiliates with different logic based on:
1. **Platform**: PROVIDER, SUBSCRIBER, WEALTH
2. **Affiliate Type**: INDIVIDUAL, ORGANIZATION

Each combination has unique requirements.

---

## ✅ Answer to Question 1: Multi-Field Routing with Composite Keys

### Implementation

```java
// Step 1: Create handler with composite key mappings
@StrategyHandler
public class CompositeAffiliateHandler {

    // Format: "PLATFORM:TYPE"
    @StrategyMapping("PROVIDER:ORGANIZATION")
    public Affiliate handleProviderOrg(CreateAffiliateRequest request) {
        // Provider + Organization specific logic:
        // - Corporate verification
        // - Tax ID validation
        // - Multi-user account setup
        return createAffiliate(request, "Provider Organization");
    }

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    public Affiliate handleProviderIndividual(CreateAffiliateRequest request) {
        // Provider + Individual specific logic:
        // - Personal KYC
        // - Single user account
        return createAffiliate(request, "Provider Individual");
    }

    @StrategyMapping("WEALTH:ORGANIZATION")
    public Affiliate handleWealthOrg(CreateAffiliateRequest request) {
        // Wealth + Organization specific logic:
        // - Financial institution verification
        // - Compliance checks
        // - Portfolio management setup
        return createAffiliate(request, "Wealth Organization");
    }

    @StrategyMapping("WEALTH:INDIVIDUAL")
    public Affiliate handleWealthIndividual(CreateAffiliateRequest request) {
        // Wealth + Individual specific logic:
        // - High net worth verification
        // - Personal wealth management
        return createAffiliate(request, "Wealth Individual");
    }

    private Affiliate createAffiliate(CreateAffiliateRequest request, String type) {
        Affiliate affiliate = new Affiliate();
        affiliate.setName(request.getName() + " (" + type + ")");
        return affiliate;
    }
}

// Step 2: Use in controller
@RestController
@RequestMapping("/affiliate")
@RequiredArgsConstructor
public class AffiliateController {

    private final StrategyDispatcher dispatcher;

    @PostMapping("/create")
    public ResponseEntity<Affiliate> createAffiliate(
            @RequestBody CreateAffiliateRequest request,
            @RequestParam String affiliateType) {

        // Get platform from request
        PlatformType platform = PlatformType.fromId(request.getPlatformId());

        // Create composite key from BOTH fields
        CompositeKey key = CompositeKey.of(platform, affiliateType);

        // Dispatcher automatically routes to correct method
        // Example: platform=PROVIDER, type=ORGANIZATION
        //   -> routes to "PROVIDER:ORGANIZATION"
        //   -> calls handleProviderOrg()
        Affiliate affiliate = dispatcher.dispatch(key, Affiliate.class, request);

        return ResponseEntity.ok(affiliate);
    }
}
```

### Usage Examples

```bash
# Create Provider Organization
curl -X POST http://localhost:8080/affiliate/create?affiliateType=ORGANIZATION \
  -H "Content-Type: application/json" \
  -d '{"platformId": 2, "name": "Acme Corp"}'
# Routes to: PROVIDER:ORGANIZATION -> handleProviderOrg()

# Create Wealth Individual
curl -X POST http://localhost:8080/affiliate/create?affiliateType=INDIVIDUAL \
  -H "Content-Type: application/json" \
  -d '{"platformId": 3, "name": "John Doe"}'
# Routes to: WEALTH:INDIVIDUAL -> handleWealthIndividual()
```

---

## ✅ Answer to Question 2: Interface-Based Approach (Like Feign)

### No Handler/Factory/Strategy Needed!

```java
// Step 1: Define interface with @StrategyClient (NO IMPLEMENTATION!)
@StrategyClient
public interface AffiliateCreationService {

    // Single-key methods
    @StrategyMapping("SUBSCRIBER")
    Affiliate createSubscriber(CreateAffiliateRequest request);

    @StrategyMapping("PROVIDER")
    Affiliate createProvider(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH")
    Affiliate createWealth(CreateAffiliateRequest request);

    // Composite-key methods
    @StrategyMapping("PROVIDER:ORGANIZATION")
    Affiliate createProviderOrganization(CreateAffiliateRequest request);

    @StrategyMapping("PROVIDER:INDIVIDUAL")
    Affiliate createProviderIndividual(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH:ORGANIZATION")
    Affiliate createWealthOrganization(CreateAffiliateRequest request);

    @StrategyMapping("WEALTH:INDIVIDUAL")
    Affiliate createWealthIndividual(CreateAffiliateRequest request);
}

// Step 2: Register as Spring bean (Configuration class)
@Configuration
public class StrategyClientConfig {

    @Bean
    public AffiliateCreationService affiliateCreationService(
            StrategyMappingRegistry registry) throws Exception {
        return new StrategyClientFactoryBean<>(
            AffiliateCreationService.class,
            registry
        ).getObject();
    }
}

// Step 3: Inject and use like any Spring bean!
@RestController
@RequestMapping("/affiliate/v2")
@RequiredArgsConstructor
public class AffiliateV2Controller {

    // Just inject the interface - Spring creates implementation!
    private final AffiliateCreationService affiliateService;

    // Explicit method calls - no dispatcher needed!
    @PostMapping("/subscriber")
    public Affiliate createSubscriber(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createSubscriber(req);
    }

    @PostMapping("/provider")
    public Affiliate createProvider(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createProvider(req);
    }

    @PostMapping("/wealth")
    public Affiliate createWealth(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createWealth(req);
    }

    // Composite key methods
    @PostMapping("/provider/org")
    public Affiliate createProviderOrg(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createProviderOrganization(req);
    }

    @PostMapping("/wealth/individual")
    public Affiliate createWealthIndividual(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createWealthIndividual(req);
    }
}
```

### How It Works

1. **You define** the interface with `@StrategyClient`
2. **Spring creates** a dynamic proxy at runtime
3. **Proxy routes** calls to methods annotated with `@StrategyMapping`
4. **You need** the actual `@StrategyHandler` to provide implementation
5. **No boilerplate** - no factory, no manual routing!

### Usage Examples

```bash
# Using interface methods directly
curl -X POST http://localhost:8080/affiliate/v2/subscriber \
  -H "Content-Type: application/json" \
  -d '{"platformId": 1, "name": "Subscriber Co"}'

curl -X POST http://localhost:8080/affiliate/v2/provider/org \
  -H "Content-Type: application/json" \
  -d '{"platformId": 2, "name": "Provider Corp"}'
```

---

## 🎯 Complete Solution: Both Features Combined

```java
// ===== 1. Handlers with composite key mappings =====
@StrategyHandler
public class CompositeAffiliateHandler {
    @StrategyMapping("PROVIDER:ORGANIZATION")
    public Affiliate handleProviderOrg(CreateAffiliateRequest req) { ... }

    @StrategyMapping("WEALTH:INDIVIDUAL")
    public Affiliate handleWealthIndividual(CreateAffiliateRequest req) { ... }

    // ... other combinations
}

// ===== 2. Interface for type-safe access (NO implementation!) =====
@StrategyClient
public interface AffiliateCreationService {
    @StrategyMapping("PROVIDER:ORGANIZATION")
    Affiliate createProviderOrganization(CreateAffiliateRequest req);

    @StrategyMapping("WEALTH:INDIVIDUAL")
    Affiliate createWealthIndividual(CreateAffiliateRequest req);
}

// ===== 3. Register interface =====
@Configuration
public class StrategyClientConfig {
    @Bean
    public AffiliateCreationService affiliateService(StrategyMappingRegistry registry) {
        return new StrategyClientFactoryBean<>(
            AffiliateCreationService.class, registry
        ).getObject();
    }
}

// ===== 4. Use either approach in controller =====
@RestController
@RequiredArgsConstructor
public class AffiliateController {

    private final StrategyDispatcher dispatcher;              // Approach 1
    private final AffiliateCreationService affiliateService;  // Approach 2

    // Approach 1: Dynamic with dispatcher
    @PostMapping("/dynamic")
    public Affiliate createDynamic(
            @RequestBody CreateAffiliateRequest req,
            @RequestParam String type) {
        CompositeKey key = CompositeKey.of(
            PlatformType.fromId(req.getPlatformId()),
            type
        );
        return dispatcher.dispatch(key, Affiliate.class, req);
    }

    // Approach 2: Type-safe with interface
    @PostMapping("/provider/org")
    public Affiliate createProviderOrg(@RequestBody CreateAffiliateRequest req) {
        return affiliateService.createProviderOrganization(req);
    }
}
```

---

## 📊 Comparison: Before vs After

### Before (Traditional Approach)
```java
// Needed:
// 1. Interface with strategy method
// 2. Factory class
// 3. Separate class per strategy (6 classes for 6 combinations!)
// 4. Manual routing logic

// Total: ~200 lines of code
```

### After (New Approach)

**Option A: Annotation + Dispatcher**
```java
// Needed:
// 1. One handler with @StrategyMapping methods
// 2. Dispatcher call

// Total: ~40 lines of code
```

**Option B: Interface-Based**
```java
// Needed:
// 1. Interface with @StrategyClient
// 2. Bean registration
// 3. One handler with implementations

// Total: ~30 lines of code
// NO factory, NO strategy classes!
```

---

## 🚀 Benefits

### Composite Keys Solve:
✅ Multi-dimensional routing (Platform + Type)
✅ Complex business rules
✅ Avoiding nested if/switch statements
✅ Clean, declarative routing

### @StrategyClient Solves:
✅ No factory boilerplate
✅ No strategy class files
✅ Interface-driven design (like Feign/HttpExchange)
✅ Type-safe method calls
✅ Self-documenting API

---

## 🎓 When to Use What?

### Use Dispatcher + Composite Keys When:
- Routing logic is dynamic (from config/database)
- Many possible combinations
- Need maximum flexibility

### Use @StrategyClient When:
- Well-defined set of operations
- Want type-safe explicit methods
- Public API definitions
- Team prefers interface-driven design

### Use Both When:
- Public API: Interface-based (clean, explicit)
- Internal: Dispatcher-based (flexible, dynamic)

---

## ✅ Summary

**Question 1: Multi-field routing?**
✅ Use `CompositeKey.of(field1, field2, ...)`
✅ Map with `@StrategyMapping("KEY1:KEY2:KEY3")`
✅ Works with all approaches

**Question 2: Avoid Factory/Handler creation?**
✅ Use `@StrategyClient` on interface
✅ Register with `StrategyClientFactoryBean`
✅ Spring creates implementation automatically
✅ Like Feign and @HttpExchange!

**Result**: Minimal code, maximum flexibility! 🎉
