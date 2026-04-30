TRUNCATE TABLE products RESTART IDENTITY CASCADE;

INSERT INTO products (name, description, price, stock, image_url, category_name)
VALUES
    ('iPhone 15', 'Apple iPhone 15 128GB', 49999.99, 20, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569', 'Telefon'),

    ('Samsung Galaxy S24', 'Samsung Galaxy S24 256GB', 44999.99, 18, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf', 'Telefon'),

    ('MacBook Air M2', 'Apple MacBook Air M2 13 inch', 42999.99, 12, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8', 'Laptop'),

    ('Lenovo ThinkPad E14', 'Lenovo ThinkPad E14 Intel i5 16GB RAM', 29999.99, 15, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853', 'Laptop'),

    ('Sony WH-1000XM5', 'Sony noise cancelling wireless headphones', 14999.99, 25, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e', 'Kulaklık'),

    ('AirPods Pro 2', 'Apple AirPods Pro 2. Nesil', 9999.99, 30, 'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434', 'Kulaklık'),

    ('Logitech MX Master 3S', 'Wireless performance mouse', 3999.99, 40, 'https://images.unsplash.com/photo-1527814050087-3793815479db', 'Mouse'),

    ('Logitech MX Keys', 'Wireless illuminated keyboard', 4999.99, 35, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3', 'Klavye'),

    ('Dell UltraSharp 27', 'Dell UltraSharp 27 inch 4K monitor', 18999.99, 10, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf', 'Monitör'),

    ('Samsung Odyssey G5', 'Samsung Odyssey G5 gaming monitor', 12999.99, 14, 'https://images.unsplash.com/photo-1593640408182-31c70c8268f5', 'Monitör'),

    ('iPad Air', 'Apple iPad Air 10.9 inch', 25999.99, 17, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0', 'Tablet'),

    ('Samsung Galaxy Tab S9', 'Samsung Galaxy Tab S9 128GB', 23999.99, 16, 'https://images.unsplash.com/photo-1561154464-82e9adf32764', 'Tablet'),

    ('Apple Watch Series 9', 'Apple Watch Series 9 GPS', 15999.99, 22, 'https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d', 'Akıllı Saat'),

    ('Samsung Galaxy Watch 6', 'Samsung Galaxy Watch 6 Bluetooth', 9999.99, 24, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30', 'Akıllı Saat'),

    ('Anker PowerCore 20000', 'Anker 20000 mAh powerbank', 2499.99, 50, 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5', 'Aksesuar');