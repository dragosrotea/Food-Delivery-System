USE FoodDeliveryDB;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Username = 'customer_demo')
BEGIN
    INSERT INTO dbo.Users (FullName, Username, Password, UserRole)
    VALUES
        ('Demo Customer', 'customer_demo', 'customer_demo', 'Customer'),
        ('Demo Driver', 'driver_demo', 'driver_demo', 'Driver'),
        ('Demo Administrator', 'admin_demo', 'admin_demo', 'Admin');
END;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Restaurants WHERE Name = 'Urban Pizza')
BEGIN
    INSERT INTO dbo.Restaurants (Name, Street, City)
    VALUES
        ('Urban Pizza', '10 Central Street', 'Cluj-Napoca'),
        ('Green Bowl', '24 Republicii Street', 'Cluj-Napoca');
END;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.MenuItems)
BEGIN
    DECLARE @PizzaRestaurantID INT =
        (SELECT RestaurantID FROM dbo.Restaurants WHERE Name = 'Urban Pizza');
    DECLARE @BowlRestaurantID INT =
        (SELECT RestaurantID FROM dbo.Restaurants WHERE Name = 'Green Bowl');

    INSERT INTO dbo.MenuItems (RestaurantID, ItemName, Price, Category)
    VALUES
        (@PizzaRestaurantID, 'Margherita', 32.00, 'Pizza'),
        (@PizzaRestaurantID, 'Diavola', 39.50, 'Pizza'),
        (@PizzaRestaurantID, 'Tiramisu', 24.00, 'Dessert'),
        (@BowlRestaurantID, 'Falafel Bowl', 36.00, 'Main'),
        (@BowlRestaurantID, 'Fresh Lemonade', 12.00, 'Drink');
END;
GO
