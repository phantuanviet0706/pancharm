CREATE TABLE Users (
    id INT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    fullname VARCHAR(100),
    avatar TEXT,
    address TEXT,
    phone VARCHAR(35),
    status SMALLINT,
    role INT,
    token TEXT,
    soft_deleted SMALLINT,
    created_at INT,
    updated_at INT
);

CREATE TABLE Company (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    avatar TEXT,
    taxcode VARCHAR(63),
    bank_attachment TEXT,
    created_at INT,
    updated_at INT
);

CREATE TABLE CompanyInfos (
    id INT PRIMARY KEY,
    address VARCHAR(255),
    phone VARCHAR(35),
    email VARCHAR(255),
    person_in_charge INT,
    company_id INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (person_in_charge) REFERENCES Users(id),
    FOREIGN KEY (company_id) REFERENCES Company(id)
);

CREATE TABLE Categories (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    slug VARCHAR(63),
    parent_id INT,
    created_at INT,
    updated_at INT
);

CREATE TABLE Products (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    slug VARCHAR(63) UNIQUE,
    quantity INT,
    unit_price INT,
    color VARCHAR(10),
    status INT,
    description TEXT,
    category_id INT,
    soft_deleted SMALLINT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (category_id) REFERENCES Categories(id)
);

CREATE TABLE ProductImages (
    id INT PRIMARY KEY,
    path VARCHAR(255),
    is_default INT,
    product_id INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE Collections (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    slug VARCHAR(63) UNIQUE,
    status INT,
    description TEXT,
    created_at INT,
    updated_at INT
);

CREATE TABLE CollectionImages (
    id INT PRIMARY KEY,
    path VARCHAR(255),
    is_default INT,
    collection_id INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (collection_id) REFERENCES Collections(id)
);

CREATE TABLE CollectionProducts (
    id INT PRIMARY KEY,
    product_id INT,
    collection_id INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (collection_id) REFERENCES Collections(id)
);

CREATE TABLE Orders (
    id INT PRIMARY KEY,
    user_id INT,
    status INT,
    total_price FLOAT,
    shipping_address_id INT,
    description TEXT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE OrderItems (
    id INT PRIMARY KEY,
    order_id INT,
    product_id INT,
    unit_price FLOAT,
    quantity INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE Payments (
    id INT PRIMARY KEY,
    order_id INT,
    payment_method VARCHAR(63),
    amount FLOAT,
    status INT,
    paid_at INT,
    transaction_code VARCHAR(100),
    resource_path VARCHAR(255),
    created_at INT,
    updated_at INT,
    FOREIGN KEY (order_id) REFERENCES Orders(id)
);

CREATE TABLE ShippingAddress (
    id INT PRIMARY KEY,
    order_id INT,
    user_id INT,
    recipient_name VARCHAR(100),
    address TEXT,
    ward VARCHAR(100),
    district VARCHAR(100),
    province VARCHAR(100),
    phone_number VARCHAR(35),
    zip_code VARCHAR(100),
    is_default INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Carts (
    id INT PRIMARY KEY,
    user_id INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE CartItems (
    id INT PRIMARY KEY,
    cart_id INT,
    product_id INT,
    quantity INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (cart_id) REFERENCES Carts(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE Blogs (
    id INT PRIMARY KEY,
    title VARCHAR(255),
    slug VARCHAR(255) UNIQUE,
    content TEXT,
    image_url VARCHAR(255),
    author_id INT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (author_id) REFERENCES Users(id)
);

CREATE TABLE Ratings (
    id INT PRIMARY KEY,
    user_id INT,
    product_id INT,
    rating INT,
    comment TEXT,
    created_at INT,
    updated_at INT,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);
