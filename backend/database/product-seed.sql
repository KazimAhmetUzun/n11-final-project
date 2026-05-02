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
    ('Anker PowerCore 20000', 'Anker 20000 mAh powerbank', 2499.99, 50, 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5', 'Aksesuar'),
    ('PlayStation 5', 'Sony PlayStation 5 oyun konsolu', 27999.99, 9, 'https://images.unsplash.com/photo-1606813907291-d86efa9b94db', 'Oyun'),
    ('Xbox Series X', 'Microsoft Xbox Series X oyun konsolu', 26999.99, 11, 'https://images.unsplash.com/photo-1621259182978-fbf93132d53d', 'Oyun'),
    ('Nintendo Switch OLED', 'Nintendo Switch OLED taşınabilir konsol', 14999.99, 13, 'https://images.unsplash.com/photo-1578303512597-81e6cc155b3e', 'Oyun'),
    ('Canon EOS R50', 'Canon EOS R50 aynasız kamera', 29999.99, 8, 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32', 'Kamera'),
    ('GoPro Hero 12', 'GoPro Hero 12 aksiyon kamerası', 15999.99, 19, 'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f', 'Kamera'),
    ('JBL Charge 5', 'JBL Charge 5 bluetooth hoparlör', 6499.99, 28, 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1', 'Hoparlör'),
    ('Kindle Paperwhite', 'Amazon Kindle Paperwhite e-kitap okuyucu', 7999.99, 21, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c', 'E-Kitap'),
    ('Asus ROG Strix G16', 'Asus ROG Strix G16 gaming laptop', 59999.99, 7, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302', 'Laptop'),
    ('Xiaomi Mi Band 8', 'Xiaomi Mi Band 8 akıllı bileklik', 1499.99, 60, 'https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6', 'Akıllı Saat');