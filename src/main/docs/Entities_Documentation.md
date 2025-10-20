# FoodSquad - Entity Documentation

## User
- **Attributes**:
  - id: Long
  - firstName: String
  - lastName: String
  - email: String
  - password: String
  - phone: String
  - role: UserRole (ADMIN, CUSTOMER, PARTNER, EMPLOYEE, CASHIER)
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime
  - enabled: boolean
- **Relationships**:
  - OneToMany: Address
  - OneToOne: Token (for authentication)

## Address
- **Attributes**:
  - id: Long
  - street: String
  - city: String
  - state: String
  - postalCode: String
  - country: Country
  - isDefault: boolean
- **Relationships**:
  - ManyToOne: User

## Store
- **Attributes**:
  - id: Long
  - name: String
  - description: String
  - logo: String
  - banner: String
  - status: StoreStatus (ACTIVE, INACTIVE, SUSPENDED)
  - openingHours: String
  - rating: Double
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime
- **Relationships**:
  - ManyToOne: Partner
  - OneToMany: Product
  - OneToMany: Menu
  - OneToMany: Review
  - OneToMany: Order

## Product
- **Attributes**:
  - id: Long
  - name: String
  - description: String
  - price: Double
  - discountPrice: Double
  - isAvailable: boolean
  - isFeatured: boolean
  - preparationTime: Integer (in minutes)
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime
- **Relationships**:
  - ManyToOne: Store
  - ManyToOne: Category
  - OneToMany: ProductOptionGroup
  - OneToMany: ProductAttribute
  - OneToMany: Media
  - OneToMany: Review
  - ManyToMany: Promotion

## Order
- **Attributes**:
  - id: Long
  - orderNumber: String
  - totalAmount: Double
  - status: OrderStatus (PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED)
  - paymentStatus: PaymentStatus (PENDING, PAID, FAILED, REFUNDED)
  - deliveryAddress: String
  - notes: String
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime
- **Relationships**:
  - ManyToOne: Customer
  - ManyToOne: Store
  - OneToMany: OrderItem
  - OneToOne: Payment
  - OneToOne: Shipping

## OrderItem
- **Attributes**:
  - id: Long
  - quantity: Integer
  - unitPrice: Double
  - totalPrice: Double
  - notes: String
- **Relationships**:
  - ManyToOne: Order
  - ManyToOne: Product
  - OneToMany: OrderItemOption

## Category
- **Attributes**:
  - id: Long
  - name: String
  - description: String
  - image: String
  - isActive: boolean
- **Relationships**:
  - OneToMany: Product

## Review
- **Attributes**:
  - id: Long
  - rating: Integer (1-5)
  - comment: String
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime
- **Relationships**:
  - ManyToOne: Customer
  - ManyToOne: Product
  - ManyToOne: Store

## Payment
- **Attributes**:
  - id: Long
  - amount: Double
  - paymentMethod: PaymentMethod (CREDIT_CARD, CASH, MOBILE_MONEY)
  - transactionId: String
  - status: PaymentStatus
  - paymentDate: LocalDateTime
- **Relationships**:
  - OneToOne: Order

## Promotion
- **Attributes**:
  - id: Long
  - name: String
  - description: String
  - discountType: DiscountType (PERCENTAGE, FIXED_AMOUNT, BUY_ONE_GET_ONE)
  - discountValue: Double
  - startDate: LocalDateTime
  - endDate: LocalDateTime
  - isActive: boolean
  - createdAt: LocalDateTime
  - updatedAt: LocalDateTime
- **Relationships**:
  - ManyToMany: Product

## Media
- **Attributes**:
  - id: Long
  - url: String
  - type: MediaType (IMAGE, VIDEO)
  - isPrimary: boolean
- **Relationships**:
  - ManyToOne: Product

## Token
- **Attributes**:
  - id: Long
  - token: String
  - tokenType: TokenType (BEARER)
  - expired: boolean
  - revoked: boolean
  - expiresAt: LocalDateTime
- **Relationships**:
  - ManyToOne: User

## ActivitySector
- **Attributes**:
  - id: Long
  - name: String
  - description: String
- **Relationships**:
  - OneToMany: Store

## Country
- **Attributes**:
  - id: Long
  - name: String
  - code: String
  - phoneCode: String
  - currency: Currency

## Currency
- **Attributes**:
  - id: Long
  - code: String (e.g., USD, EUR)
  - name: String
  - symbol: String

## User Role Enums
- **UserRole**: ADMIN, CUSTOMER, PARTNER, EMPLOYEE, CASHIER
- **OrderStatus**: PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
- **PaymentStatus**: PENDING, PAID, FAILED, REFUNDED
- **PaymentMethod**: CREDIT_CARD, CASH, MOBILE_MONEY
- **MediaType**: IMAGE, VIDEO
- **DiscountType**: PERCENTAGE, FIXED_AMOUNT, BUY_ONE_GET_ONE
- **StoreStatus**: ACTIVE, INACTIVE, SUSPENDED
