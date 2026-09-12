IF DB_ID('FoodDeliveryDB') IS NULL
BEGIN
    CREATE DATABASE FoodDeliveryDB;
END;
GO

USE FoodDeliveryDB;
GO

IF OBJECT_ID('dbo.Users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Users (
        UserID INT IDENTITY(1,1) PRIMARY KEY,
        FullName NVARCHAR(120) NOT NULL,
        Username NVARCHAR(60) NOT NULL UNIQUE,
        Password NVARCHAR(255) NOT NULL,
        UserRole NVARCHAR(20) NOT NULL,
        CONSTRAINT CK_Users_Role CHECK (UserRole IN ('Customer', 'Driver', 'Admin'))
    );
END;
GO

IF OBJECT_ID('dbo.Restaurants', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Restaurants (
        RestaurantID INT IDENTITY(1,1) PRIMARY KEY,
        Name NVARCHAR(120) NOT NULL UNIQUE,
        Street NVARCHAR(160) NOT NULL,
        City NVARCHAR(100) NOT NULL
    );
END;
GO

IF OBJECT_ID('dbo.MenuItems', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.MenuItems (
        ItemID INT IDENTITY(1,1) PRIMARY KEY,
        RestaurantID INT NOT NULL,
        ItemName NVARCHAR(120) NOT NULL,
        Price DECIMAL(10,2) NOT NULL,
        Category NVARCHAR(60) NOT NULL,
        CONSTRAINT CK_MenuItems_Price CHECK (Price >= 0),
        CONSTRAINT FK_MenuItems_Restaurants
            FOREIGN KEY (RestaurantID) REFERENCES dbo.Restaurants(RestaurantID)
            ON DELETE CASCADE
    );
END;
GO

IF OBJECT_ID('dbo.Orders', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Orders (
        OrderID INT IDENTITY(1,1) PRIMARY KEY,
        CustomerID INT NOT NULL,
        RestaurantID INT NOT NULL,
        DriverID INT NULL,
        Status NVARCHAR(30) NOT NULL
            CONSTRAINT DF_Orders_Status DEFAULT 'Pending',
        CreatedAt DATETIME2 NOT NULL
            CONSTRAINT DF_Orders_CreatedAt DEFAULT SYSUTCDATETIME(),
        CONSTRAINT CK_Orders_Status CHECK (Status IN ('Pending', 'Arriving', 'Delivered')),
        CONSTRAINT FK_Orders_Customers FOREIGN KEY (CustomerID) REFERENCES dbo.Users(UserID),
        CONSTRAINT FK_Orders_Drivers FOREIGN KEY (DriverID) REFERENCES dbo.Users(UserID),
        CONSTRAINT FK_Orders_Restaurants FOREIGN KEY (RestaurantID) REFERENCES dbo.Restaurants(RestaurantID)
    );
END;
GO

IF OBJECT_ID('dbo.OrderDetails', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.OrderDetails (
        OrderDetailID INT IDENTITY(1,1) PRIMARY KEY,
        OrderID INT NOT NULL,
        ItemID INT NOT NULL,
        Quantity INT NOT NULL CONSTRAINT DF_OrderDetails_Quantity DEFAULT 1,
        CONSTRAINT CK_OrderDetails_Quantity CHECK (Quantity > 0),
        CONSTRAINT FK_OrderDetails_Orders
            FOREIGN KEY (OrderID) REFERENCES dbo.Orders(OrderID)
            ON DELETE CASCADE,
        CONSTRAINT FK_OrderDetails_MenuItems FOREIGN KEY (ItemID) REFERENCES dbo.MenuItems(ItemID)
    );
END;
GO
