# Hướng dẫn chạy dự án kiến trúc Space-Based

## Yêu cầu hệ thống
- Docker và Docker Compose đã được cài đặt.
- (Tùy chọn) Java 17 và Maven nếu bạn muốn chạy local không qua Docker.

## Các module (Processing Units)
Dựa theo thiết kế Space-Based Architecture, hệ thống chia thành các PU:
1. **Product PU** (Port 8081): Quản lý sản phẩm. Đọc từ Redis.
2. **Cart PU** (Port 8082): Quản lý giỏ hàng. Lưu session trên Redis.
3. **Order PU** (Port 8083): Xử lý checkout, lấy cart từ Redis và gọi Inventory PU.
4. **Inventory PU** (Port 8084): Xử lý giảm số lượng tồn kho trực tiếp qua cache (Hazelcast/Redis).

## Cấu trúc dữ liệu Data Grid
- Hệ thống sử dụng Redis cho dữ liệu session, cache, và Hazelcast cho Data Grid phân tán tuỳ chỉnh.
- Không sử dụng Database truyền thống để đảm bảo tải cao, độ trễ thấp (low latency).

## Chạy hệ thống bằng Docker Compose
Mở terminal tại thư mục gốc của dự án (cùng cấp với file `docker-compose.yml`) và chạy:

```bash
docker-compose up --build
```

Lệnh này sẽ khởi động:
- Redis server
- Hazelcast server
- Các Processing Units (Product, Cart, Order, Inventory) theo từng port riêng.

## Kịch bản Test Demo
Các tính năng xử lý hoàn toàn không phụ thuộc Database (no bottleneck).

### 1. Load danh sách sản phẩm
- **Endpoint**: `GET http://localhost:8081/products`
- **Mô tả**: Xem danh sách sản phẩm nhanh được dump vào Redis.

### 2. Thêm vào giỏ hàng
- **Endpoint**: `POST http://localhost:8082/cart/add`
- **Body** (JSON): `{"userId": "1", "productId": "p1", "quantity": 1}`
- **Mô tả**: Cart lưu trong Redis (Data Grid).

### 3. Xem giỏ hàng
- **Endpoint**: `GET http://localhost:8082/cart?userId=1`

### 4. Kiểm tra tồn kho (Trước khi đặt)
- **Endpoint**: `GET http://localhost:8084/stock/p1`

### 5. Đặt hàng (Checkout)
- **Endpoint**: `POST http://localhost:8083/checkout`
- **Body** (JSON): `{"userId": "1"}`
- **Mô tả**: Order PU lấy giỏ hàng từ Redis, gọi hàm giảm tồn (hoặc event) bên Inventory. Inventory giảm stock và trả về ngay. Không chờ DB ghi dữ liệu.

### 6. Kiểm tra lại tồn kho (Sau khi đặt)
- **Endpoint**: `GET http://localhost:8084/stock/p1`
- **Kỳ vọng**: Số lượng giảm ngay lập tức.

