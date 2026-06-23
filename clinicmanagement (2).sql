-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 21, 2026 lúc 04:18 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `clinicmanagement`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','PATIENT','RECEPTIONIST') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','BANNED') DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`id`, `username`, `password_hash`, `role`, `status`, `created_at`, `email`) VALUES
(4, 'admin4', '$2a$10$Fxui/yf9VD2innRbi7l63.GPtozej9cWz1745LsKEHiTp/jYAA632', 'ADMIN', 'ACTIVE', '2026-05-07 15:20:32', 'admin4@gmail.com'),
(5, 'patient1', '$2a$10$iV2juvunTID5qf2k5t4kauHNM4HkKBTpKN4fJw9s2HRwe/1zyN6ai', 'PATIENT', 'ACTIVE', '2026-05-07 18:42:37', 'patient1@gmail.com'),
(6, 'patient2', '$2a$10$DZRuOEFULDZ79ZAyJZdFf.c9JieJSTytTwx5BF5RNqj/029wj8E6K', 'PATIENT', 'ACTIVE', '2026-05-08 10:37:45', 'patient2@gmail.com'),
(7, 'admin6', '$2a$10$klHyPqf/g7DjO3s0EuhzLOQ4C5xEro3QsCH6xNkIurDe.dKzPHhh6', 'ADMIN', 'BANNED', '2026-05-08 15:26:05', 'admin7@gmail.com'),
(8, 'admin5', '$2a$10$u273faOb8Ku7WaSqeSuOAecN5bPtVOV0hwJw0dqz3IhSxXGXkqmIm', 'ADMIN', 'ACTIVE', '2026-05-09 16:32:10', 'admin5@gmail.com'),
(9, 'patient3', '$2a$10$xBCsZCMXeu977MjeE3Zu4eg5iyes25KuN7YqkgGORl40794iurylK', 'PATIENT', 'ACTIVE', '2026-05-09 17:14:27', 'sachdo2803@gmail.com'),
(10, 'thai123', '$2a$10$30KwpdhNICbfXFUx61eCsuw7iY44xKuiNSObpKso6ws7dDCSxCw96', 'PATIENT', 'ACTIVE', '2026-05-10 16:21:42', 'callmejeff2803@gmail.com'),
(12, 'patient4', '$2a$10$KTJ44Ma15ngdhUdnnFeQIeEYOg6BTk0rfLok33irPD3EUfrmCLd.q', 'PATIENT', 'ACTIVE', '2026-05-12 09:25:48', 'tranleminh2803@gmail.com'),
(15, 'patient5', '$2a$10$Pyzzo4edRt07wgz7Fxliq.dq1SXrFoRFyJ0pG3Yx8nEgxe/zuvbaS', 'PATIENT', 'ACTIVE', '2026-05-14 21:51:11', 'minh280304@gmail.com'),
(23, 'repceptionist1', '$2a$10$4upChGz.6Tyq/maWzKKb3.fCDeacrr2hArabXGRovKuzdS/gMvGOm', 'RECEPTIONIST', 'ACTIVE', '2026-05-25 20:26:27', 'tranleminh280304@gmail.com'),
(25, 'repceptionist2', '$2a$10$AQuMclYh0fP3rV0/RWiKKOqpisDJooOVv9F2KKoahJC3V3Y8dD5m.', 'PATIENT', 'INACTIVE', '2026-05-27 08:42:51', '1234@gmail.com');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `admin`
--

CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `admin`
--

INSERT INTO `admin` (`id`, `full_name`, `account_id`, `phone_number`, `gender`, `date_of_birth`, `email`) VALUES
(1, 'Tran Van B', 8, '0123456789', 'MALE', '2000-01-01', 'admin5@gmail.com');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `appointment`
--

CREATE TABLE `appointment` (
  `id` bigint(20) NOT NULL,
  `appointment_status` enum('HOLD','CONFIRMED','CANCELLED','COMPLETE') DEFAULT NULL,
  `medical_status` enum('NOT_EXAMINED','IN_PROGRESS','DONE') DEFAULT NULL,
  `service_type` enum('GENERAL','SPECIAL') DEFAULT NULL,
  `patient_id` bigint(20) DEFAULT NULL,
  `doctor_id` bigint(20) DEFAULT NULL,
  `time_slot_rule_id` bigint(20) DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `appointment_date` date DEFAULT NULL,
  `hold_expired_at` datetime DEFAULT NULL,
  `description` text DEFAULT NULL,
  `is_reminded` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `appointment`
--

INSERT INTO `appointment` (`id`, `appointment_status`, `medical_status`, `service_type`, `patient_id`, `doctor_id`, `time_slot_rule_id`, `start_time`, `end_time`, `appointment_date`, `hold_expired_at`, `description`, `is_reminded`) VALUES
(32, 'CONFIRMED', 'NOT_EXAMINED', NULL, 6, 6, 2, '07:30:00', '07:50:00', '2026-05-16', NULL, NULL, 0),
(33, 'CONFIRMED', 'NOT_EXAMINED', NULL, 6, 5, 1, '16:00:00', '16:30:00', '2026-05-24', NULL, '123', 1),
(34, 'CONFIRMED', 'NOT_EXAMINED', NULL, 6, 5, 1, '07:30:00', '07:45:00', '2026-05-29', NULL, NULL, NULL),
(35, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 6, 2, '07:50:00', '08:10:00', '2026-05-29', '2026-05-27 09:33:02', NULL, NULL),
(36, 'CONFIRMED', 'IN_PROGRESS', NULL, 6, 5, 1, '07:30:00', '07:45:00', '2026-05-27', NULL, NULL, NULL),
(37, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 8, 4, '07:50:00', '08:10:00', '2026-05-27', '2026-05-27 10:10:39', NULL, NULL),
(38, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 8, 4, '07:30:00', '07:50:00', '2026-05-28', '2026-05-27 16:05:05', NULL, NULL),
(39, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 5, 1, '07:45:00', '08:00:00', '2026-05-27', '2026-05-27 16:14:59', NULL, NULL),
(40, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 6, 2, '07:30:00', '07:50:00', '2026-05-28', '2026-05-27 16:15:27', NULL, NULL),
(41, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 7, 3, '08:10:00', '08:30:00', '2026-05-27', '2026-05-27 16:15:35', NULL, NULL),
(42, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 5, 1, '07:30:00', '07:45:00', '2026-06-27', '2026-06-19 00:10:37', '123', NULL),
(43, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 5, 1, '07:45:00', '08:00:00', '2026-06-27', NULL, '123', NULL),
(44, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 5, 1, '07:30:00', '07:45:00', '2026-06-20', '2026-06-19 00:44:40', NULL, NULL),
(45, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 5, 1, '07:30:00', '07:45:00', '2026-06-21', '2026-06-20 21:23:26', NULL, NULL),
(46, 'CONFIRMED', 'DONE', NULL, 9, 5, 1, '07:30:00', '07:45:00', '2026-06-21', NULL, NULL, NULL),
(47, 'CONFIRMED', 'DONE', NULL, 9, 5, 1, '07:45:00', '17:00:00', '2026-06-21', NULL, NULL, NULL),
(50, 'CANCELLED', 'NOT_EXAMINED', NULL, 9, 6, 2, '07:30:00', '07:50:00', '2026-06-22', '2026-06-20 22:06:25', NULL, NULL),
(51, 'CANCELLED', 'NOT_EXAMINED', NULL, 9, 6, 2, '07:30:00', '07:50:00', '2026-06-22', '2026-06-21 08:23:23', NULL, NULL),
(52, 'CANCELLED', 'NOT_EXAMINED', NULL, 9, 6, 2, '07:50:00', '08:10:00', '2026-06-22', '2026-06-21 08:24:23', NULL, NULL),
(53, 'CANCELLED', 'NOT_EXAMINED', NULL, 9, 6, 2, '08:10:00', '08:30:00', '2026-06-22', '2026-06-21 08:25:20', NULL, NULL),
(54, 'CANCELLED', 'NOT_EXAMINED', NULL, 9, 5, 1, '08:30:00', '08:45:00', '2026-06-22', '2026-06-21 08:25:45', NULL, NULL),
(55, 'CONFIRMED', 'NOT_EXAMINED', NULL, 9, 6, 2, '07:30:00', '07:50:00', '2026-06-25', NULL, NULL, NULL),
(56, 'CONFIRMED', 'NOT_EXAMINED', NULL, 9, 31, 8, '07:30:00', '17:00:00', '2026-06-22', NULL, NULL, NULL),
(57, 'CONFIRMED', 'NOT_EXAMINED', NULL, 6, 30, 8, '07:30:00', '08:00:00', '2026-06-22', NULL, 'đau răng', NULL),
(58, 'CANCELLED', 'NOT_EXAMINED', NULL, 6, 30, 8, '08:00:00', '08:30:00', '2026-06-22', '2026-06-21 09:02:15', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chat_message`
--

CREATE TABLE `chat_message` (
  `id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `sender_type` enum('USER','ASSISTANT') NOT NULL,
  `content` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chat_message`
--

INSERT INTO `chat_message` (`id`, `account_id`, `sender_type`, `content`, `created_at`) VALUES
(9, 12, 'USER', 'Alo', '2026-06-21 01:51:21'),
(10, 12, 'ASSISTANT', 'Chào bạn! Tôi có thể giúp gì cho bạn hôm nay? Bạn đang cần tư vấn chuyên khoa nào ạ?', '2026-06-21 01:51:22'),
(11, 12, 'USER', 'Tôi muốn đặt lịch', '2026-06-21 01:51:31'),
(12, 12, 'ASSISTANT', 'Bạn vui lòng cho biết bạn muốn khám chuyên khoa nào và lịch thời gian dự kiến để tôi hỗ trợ đặt lịch phù hợp nhé.', '2026-06-21 01:51:31'),
(13, 12, 'USER', 'Răng hàm mặt vào ngày 22', '2026-06-21 01:51:37'),
(14, 12, 'ASSISTANT', 'Cảm ơn bạn đã cung cấp thông tin. Bạn muốn đặt lịch khám chuyên khoa Răng Hàm Mặt vào ngày 22, bạn muốn chọn bác sĩ Hoàng Gia Bảo hay Nguyễn Thị Mỹ Linh, hoặc để chúng tôi sắp xếp bác sĩ phù hợp? Bạn vui lòng cho biết khung giờ bạn mong muốn để chúng tôi hỗ trợ đặt lịch nhé.', '2026-06-21 01:51:37'),
(15, 12, 'USER', 'Nguyễn thị mỹ linh', '2026-06-21 01:51:48'),
(16, 12, 'ASSISTANT', 'Bạn muốn khám bác sĩ nào?\n\nDanh sách bác sĩ còn lịch:\n\n- Hoàng Gia Bảo\n- Nguyễn Thị Mỹ Linh\n', '2026-06-21 01:51:48'),
(17, 12, 'USER', 'Nguyễn Thị Mỹ Linh', '2026-06-21 01:51:53'),
(18, 12, 'ASSISTANT', 'Không thể đặt lịch khám chuyên khoa này ngày hôm nay, vui lòng chọn ngày khác', '2026-06-21 01:51:53'),
(19, 12, 'USER', 'Hoàng Gia Bảo', '2026-06-21 01:51:59'),
(20, 12, 'ASSISTANT', 'Không thể đặt lịch khám chuyên khoa này ngày hôm nay, vui lòng chọn ngày khác', '2026-06-21 01:51:59'),
(21, 12, 'USER', 'tôi muốn đặt lịch khám răng hàm mặt của bác sĩ hoàng gia bảo vào ngày 22', '2026-06-21 01:52:15'),
(22, 12, 'ASSISTANT', '<div style=\'line-height:1.8\'>\n<b>Đặt lịch khám thành công.</b><br><br>\n\n<b>Chuyên khoa:</b> Răng Hàm Mặt<br>\n<b>Ngày khám:</b> 2026-06-22<br>\n<b>Giờ khám:</b> 08:00 - 08:30<br><br>\n\n<a href=\"https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=20000000&vnp_Command=pay&vnp_CreateDate=20260621085215&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=Thanh+toan+lich+kham+%2358&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fpayment%2Fvnpay-return&vnp_TmnCode=YBFMAF9B&vnp_TxnRef=1782006735611&vnp_Version=2.1.0&vnp_SecureHash=ac84bec46ee8233411d11ce7ce2be595c76dafff99e4a1b7a8add36d43a1026091d126f3ddc998a6b2975c857ad347ca398199f2fac9f67f1a43d4559f393ee7\" target=\"_blank\"\nstyle=\"\n        background:#1677ff;\n        color:white;\n        padding:10px 16px;\n        border-radius:8px;\n        text-decoration:none;\n        display:inline-block;\n        font-weight:bold;\n\">\nThanh toán VNPay\n</a>\n</div>\n', '2026-06-21 01:52:15'),
(23, 12, 'USER', 'Tôi muốn đặt lịch', '2026-06-21 02:05:14'),
(24, 12, 'ASSISTANT', 'Bạn vui lòng cho biết khung giờ mong muốn trong ngày 22 để tôi hỗ trợ đặt lịch khám với bác sĩ Hoàng Gia Bảo nhé.', '2026-06-21 02:05:14'),
(25, 12, 'USER', 'Không , tôi muốn đặt lịch ngày 22, khoa răng hàm mặt', '2026-06-21 02:05:33'),
(26, 12, 'ASSISTANT', 'Bạn vui lòng cho biết thời gian khám cụ thể trong ngày 22 (buổi sáng hay buổi chiều) để tôi hỗ trợ đặt lịch với bác sĩ Hoàng Gia Bảo khoa Răng Hàm Mặt nhé.', '2026-06-21 02:05:33'),
(27, 12, 'USER', 'tôi muốn đặt bác sĩ khác, có nhưng bác sĩ nào', '2026-06-21 02:05:49'),
(28, 12, 'ASSISTANT', 'Bạn muốn khám bác sĩ nào?\n\nDanh sách bác sĩ còn lịch:\n\n- Hoàng Gia Bảo\n', '2026-06-21 02:05:49'),
(29, 12, 'USER', 'thế ngày 23 thì sao', '2026-06-21 02:05:58'),
(30, 12, 'ASSISTANT', 'Bạn muốn khám bác sĩ nào?\n\nDanh sách bác sĩ còn lịch:\n\n- Hoàng Gia Bảo\n- Nguyễn Thị Mỹ Linh\n', '2026-06-21 02:05:58'),
(31, 12, 'USER', 'ngày 22', '2026-06-21 02:06:02'),
(32, 12, 'ASSISTANT', 'Bạn muốn khám bác sĩ nào?\n\nDanh sách bác sĩ còn lịch:\n\n- Hoàng Gia Bảo\n', '2026-06-21 02:06:02'),
(33, 23, 'USER', 'Xem toàn bộ lịch khám', '2026-06-21 02:16:29'),
(34, 23, 'ASSISTANT', 'Chào bạn, Trung tâm y tế quận Thanh Xuân làm việc từ 7h30 đến 17h hàng ngày. Bạn vui lòng cho biết chuyên khoa hoặc bác sĩ bạn quan tâm để mình hỗ trợ xem lịch lịch khám cụ thể và hướng dẫn đặt lịch giúp bạn nhé!', '2026-06-21 02:16:29'),
(35, 23, 'USER', 'Xem toàn bộ lịch khám', '2026-06-21 02:16:36'),
(36, 23, 'ASSISTANT', 'Xin lỗi bạn, Trung tâm hiện chưa có lịch khám chi tiết từng bác sĩ công khai. Bạn cần tư vấn chuyên khoa hoặc bác sĩ nào, mình sẽ hỗ trợ đặt lịch phù hợp nhé. Hoặc bạn có thể gọi đến số 0243.858.7155 để biết thông tin cụ thể hơn.', '2026-06-21 02:16:36');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `department`
--

CREATE TABLE `department` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `department`
--

INSERT INTO `department` (`id`, `name`, `description`) VALUES
(1, 'Nội khoa', 'Khám và điều trị các bệnh nội khoa'),
(2, 'Ngoại khoa', 'Khám và điều trị ngoại khoa'),
(3, 'Tim mạch', 'Khám và điều trị bệnh tim mạch'),
(4, 'Xương khớp', 'Khám và điều trị bệnh cơ xương khớp'),
(5, 'Da liễu', 'Khám và điều trị bệnh da liễu'),
(6, 'Mắt', 'Khám và điều trị các bệnh về mắt'),
(7, 'Tai Mũi Họng', 'Khám và điều trị tai mũi họng'),
(8, 'Răng Hàm Mặt', 'Khám và điều trị răng hàm mặt'),
(9, 'Sản phụ khoa', 'Khám và điều trị sản phụ khoa'),
(10, 'Y học cổ truyền', 'Điều trị bằng y học cổ truyền'),
(11, 'Phục hồi chức năng', 'Điều trị phục hồi chức năng'),
(12, 'Tâm thần', 'Khám và điều trị tâm thần'),
(13, 'HIV/AIDS', 'Tư vấn và điều trị HIV/AIDS'),
(14, 'Chẩn đoán hình ảnh', 'Xét nghiệm và chẩn đoán hình ảnh');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `doctor`
--

CREATE TABLE `doctor` (
  `id` bigint(20) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `years_of_experience` int(11) DEFAULT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `doctor`
--

INSERT INTO `doctor` (`id`, `full_name`, `date_of_birth`, `gender`, `phone_number`, `years_of_experience`, `department_id`, `image_url`) VALUES
(5, 'Lương Thị Bích Ngọc', '1978-05-12', 'Nữ', '0901000003', 18, 1, '/uploads/1779612900156_f132c29d-e743-4565-a8b0-b68d84daa571-VT1248.-LÆ°Æ¡ng-Tha»_-BA_ch.webp'),
(6, 'Nguyễn Dương Thúy Hằng', '1982-09-20', 'Nữ', '0901000002', 15, 2, '/uploads/1779612994125_images (1).jpg'),
(7, 'Ngô Tùng Dương', '1985-11-11', 'Nam', '0901000003', 12, 3, '/uploads/1779613308106_Bac-si-Bao-scaled.jpg'),
(8, 'Đặng Thị Thanh Hà', '1979-07-08', 'Nữ', '0901000004', 17, 4, NULL),
(9, 'Huỳnh Thị Dung', '1977-03-15', 'Nữ', '0901000005', 20, 5, NULL),
(10, 'Nguyễn Minh Hải', '1975-01-25', 'Nữ', '0901000006', 22, 6, NULL),
(13, 'Nguyễn Văn An', '1985-03-12', 'Nam', '0901000101', 12, 1, NULL),
(14, 'Trần Thị Bình', '1990-07-22', 'Nữ', '0901000102', 8, 1, NULL),
(15, 'Lê Văn Cường', '1992-11-05', 'Nam', '0901000103', 6, 1, NULL),
(16, 'Phạm Minh Tuấn', '1980-01-15', 'Nam', '0901000201', 15, 2, NULL),
(17, 'Nguyễn Thị Hồng', '1988-06-18', 'Nữ', '0901000202', 10, 2, NULL),
(18, 'Đỗ Văn Long', '1991-09-09', 'Nam', '0901000203', 7, 2, NULL),
(19, 'Hoàng Thị Mai', '1987-02-14', 'Nữ', '0901000301', 9, 3, NULL),
(20, 'Nguyễn Văn Đức', '1993-12-01', 'Nam', '0901000302', 5, 3, NULL),
(21, 'Trần Minh Khôi', '1983-08-08', 'Nam', '0901000401', 13, 4, NULL),
(22, 'Lý Thị Lan', '1995-04-25', 'Nữ', '0901000402', 4, 4, NULL),
(23, 'Phan Văn Dũng', '1982-10-10', 'Nam', '0901000501', 14, 5, NULL),
(24, 'Nguyễn Thị Hạnh', '1989-03-03', 'Nữ', '0901000502', 9, 5, NULL),
(25, 'Nguyễn Văn Phúc', '1986-05-10', 'Nam', '0902000601', 10, 6, NULL),
(26, 'Trần Thị Ngọc', '1991-08-21', 'Nữ', '0902000602', 6, 6, NULL),
(27, 'Lê Minh Hoàng', '1984-02-17', 'Nam', '0902000701', 12, 7, NULL),
(28, 'Phạm Thị Lan Anh', '1993-11-30', 'Nữ', '0902000702', 5, 7, NULL),
(29, 'Đỗ Văn Hải', '1989-07-09', 'Nam', '0902000703', 8, 7, NULL),
(30, 'Hoàng Gia Bảo', '1990-03-03', 'Nam', '0902000801', 7, 8, NULL),
(31, 'Nguyễn Thị Mỹ Linh', '1995-06-14', 'Nữ', '0902000802', 4, 8, NULL),
(32, 'Nguyễn Văn Khánh', '1987-01-12', 'Nam', '0903000901', 9, 9, NULL),
(33, 'Trần Thị Thu', '1992-05-23', 'Nữ', '0903000902', 6, 9, NULL),
(34, 'Lê Quang Huy', '1985-09-09', 'Nam', '0903001001', 11, 10, NULL),
(35, 'Phạm Thị Ngân', '1993-02-18', 'Nữ', '0903001002', 5, 10, NULL),
(36, 'Đỗ Minh Tâm', '1990-07-07', 'Nam', '0903001003', 7, 10, NULL),
(37, 'Hoàng Văn Sơn', '1988-11-11', 'Nam', '0903001101', 8, 11, NULL),
(38, 'Nguyễn Thị Bích', '1994-03-03', 'Nữ', '0903001102', 4, 11, NULL),
(39, 'Phan Quốc Việt', '1982-06-06', 'Nam', '0903001201', 13, 12, NULL),
(40, 'Trần Thị Kim', '1991-12-12', 'Nữ', '0903001202', 6, 12, NULL),
(41, 'Lý Văn Hòa', '1989-08-08', 'Nam', '0903001203', 9, 12, NULL),
(42, 'Nguyễn Đức Thành', '1986-04-14', 'Nam', '0903001301', 10, 13, NULL),
(43, 'Đặng Thị Mai', '1995-10-20', 'Nữ', '0903001302', 3, 13, NULL),
(44, 'Võ Minh Trí', '1983-07-01', 'Nam', '0903001401', 12, 14, NULL),
(45, 'Nguyễn Thị Huyền', '1992-09-19', 'Nữ', '0903001402', 6, 14, NULL),
(46, 'Trương Gia Bảo', '1990-01-25', 'Nam', '0903001403', 8, 14, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `dummy_enum_holder`
--

CREATE TABLE `dummy_enum_holder` (
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `email_verification`
--

CREATE TABLE `email_verification` (
  `id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `verify_token` varchar(255) NOT NULL,
  `expired_at` datetime NOT NULL,
  `is_used` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `email_verification_otp`
--

CREATE TABLE `email_verification_otp` (
  `id` bigint(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `otp_code` varchar(10) NOT NULL,
  `expired_at` datetime NOT NULL,
  `verified` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `email_verification_otp`
--

INSERT INTO `email_verification_otp` (`id`, `email`, `otp_code`, `expired_at`, `verified`) VALUES
(2, 'callmejeff2803@gmail.com', '570505', '2026-05-10 16:53:41', 1),
(4, 'tranleminh2803@gmail.com', '149772', '2026-05-12 09:30:49', 1),
(7, 'minh280304@gmail.com', '396058', '2026-05-14 21:56:11', 1),
(8, 'tranleminh280304@gmail.com', '308014', '2026-05-25 20:31:27', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notification`
--

CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `notification`
--

INSERT INTO `notification` (`id`, `account_id`, `title`, `message`, `is_read`, `created_at`) VALUES
(1, 12, 'Nhắc lịch khám', 'Bạn có lịch khám lúc 16:00 ngày 2026-05-24', 0, '2026-05-24 15:44:43');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `password_reset_otp`
--

CREATE TABLE `password_reset_otp` (
  `id` bigint(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `otp_code` varchar(10) DEFAULT NULL,
  `expired_at` datetime DEFAULT NULL,
  `verified` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `password_reset_otp`
--

INSERT INTO `password_reset_otp` (`id`, `email`, `otp_code`, `expired_at`, `verified`) VALUES
(17, 'sachdo2803@gmail.com', '465163', '2026-05-15 08:52:05', 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `patient`
--

CREATE TABLE `patient` (
  `id` bigint(20) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `patient`
--

INSERT INTO `patient` (`id`, `full_name`, `date_of_birth`, `gender`, `phone_number`, `account_id`) VALUES
(1, 'Minh Trần', '2026-06-18', 'Nam', '09888888', 5),
(2, 'Trần Văn B', '2026-06-02', 'Nam', '0141412412', 6),
(3, 'Minh Trần', '2026-06-01', 'Nam', '0988888888', 9),
(4, 'Hoang Thai', NULL, 'Nam', '0123456789', 10),
(6, 'Minh Trần', '2019-01-18', 'Nam', '123456', 12),
(9, 'Minh', NULL, 'Nam', '123456', 15),
(10, 'Trần Minh', '2025-09-02', 'Nam', '123456789', 23),
(11, 'trần minh', '1993-05-11', 'Nam', '123456', 25);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `payment`
--

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL,
  `transaction_ref` varchar(100) DEFAULT NULL,
  `amount` bigint(20) NOT NULL,
  `status` enum('HOLD','SUCCESS','FAILED') NOT NULL DEFAULT 'HOLD',
  `vnp_transaction_no` varchar(100) DEFAULT NULL,
  `order_info` varchar(255) DEFAULT NULL,
  `appointment_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `payment`
--

INSERT INTO `payment` (`id`, `transaction_ref`, `amount`, `status`, `vnp_transaction_no`, `order_info`, `appointment_id`, `created_at`) VALUES
(29, '1778817710172', 200000, 'SUCCESS', '15539536', 'Thanh toan lich kham #32', 32, '2026-05-15 11:01:50'),
(30, '1779610595350', 200000, 'SUCCESS', '15553534', 'Thanh toan lich kham #33', 33, '2026-05-24 15:16:35'),
(31, '1779848504721', 200000, 'SUCCESS', '15557547', 'Thanh toan lich kham #34', 34, '2026-05-27 09:21:44'),
(32, '1779848582267', 200000, 'HOLD', NULL, 'Thanh toan lich kham #35', 35, '2026-05-27 09:23:02'),
(33, '1779850270096', 200000, 'SUCCESS', '15557604', 'Thanh toan lich kham #36', 36, '2026-05-27 09:51:10'),
(34, '1779850839994', 200000, 'HOLD', NULL, 'Thanh toan lich kham #37', 37, '2026-05-27 10:00:39'),
(35, '1779872105497', 200000, 'HOLD', NULL, 'Thanh toan lich kham #38', 38, '2026-05-27 15:55:05'),
(36, '1779872699783', 200000, 'HOLD', NULL, 'Thanh toan lich kham #39', 39, '2026-05-27 16:04:59'),
(37, '1779872727415', 200000, 'HOLD', NULL, 'Thanh toan lich kham #40', 40, '2026-05-27 16:05:27'),
(38, '1779872735398', 200000, 'HOLD', NULL, 'Thanh toan lich kham #41', 41, '2026-05-27 16:05:35'),
(39, '1781802037875', 200000, 'HOLD', NULL, 'Thanh toan lich kham #42', 42, '2026-06-19 00:00:37'),
(40, '1781802063105', 200000, 'SUCCESS', '15589267', 'Thanh toan lich kham #43', 43, '2026-06-19 00:01:03'),
(41, '1781804080408', 200000, 'HOLD', NULL, 'Thanh toan lich kham #44', 44, '2026-06-19 00:34:40'),
(42, '1781964806582', 200000, 'HOLD', NULL, 'Thanh toan lich kham #45', 45, '2026-06-20 21:13:26'),
(43, '1781966070005', 200000, 'SUCCESS', '15591506', 'Thanh toan lich kham #46', 46, '2026-06-20 21:34:30'),
(44, '1781966218760', 200000, 'SUCCESS', '15591507', 'Thanh toan lich kham #47', 47, '2026-06-20 21:36:58'),
(47, '1781967385969', 200000, 'HOLD', NULL, 'Thanh toan lich kham #50', 50, '2026-06-20 21:56:25'),
(49, '1782004463243', 200000, 'HOLD', NULL, 'Thanh toan lich kham #52', 52, '2026-06-21 08:14:23'),
(50, '1782004520463', 200000, 'HOLD', NULL, 'Thanh toan lich kham #53', 53, '2026-06-21 08:15:20'),
(51, '1782004545520', 200000, 'HOLD', NULL, 'Thanh toan lich kham #54', 54, '2026-06-21 08:15:45'),
(52, '1782005424019', 200000, 'SUCCESS', '15591770', 'Thanh toan lich kham #55', 55, '2026-06-21 08:30:24'),
(53, '1782006314149', 200000, 'SUCCESS', '15591775', 'Thanh toan lich kham #56', 56, '2026-06-21 08:45:14'),
(54, '1782006458322', 200000, 'SUCCESS', '15591779', 'Thanh toan lich kham #57', 57, '2026-06-21 08:47:38'),
(55, '1782006735611', 200000, 'HOLD', NULL, 'Thanh toan lich kham #58', 58, '2026-06-21 08:52:15');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `time_slot_rule`
--

CREATE TABLE `time_slot_rule` (
  `id` bigint(20) NOT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `slot_duration` int(11) NOT NULL DEFAULT 15 COMMENT 'Đơn vị phút',
  `working_start` time NOT NULL DEFAULT '08:00:00',
  `working_end` time NOT NULL DEFAULT '17:00:00',
  `lunch_start` time DEFAULT '12:00:00',
  `lunch_end` time DEFAULT '13:00:00',
  `max_patient_per_day` int(11) DEFAULT 40
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `time_slot_rule`
--

INSERT INTO `time_slot_rule` (`id`, `department_id`, `slot_duration`, `working_start`, `working_end`, `lunch_start`, `lunch_end`, `max_patient_per_day`) VALUES
(1, 1, 15, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 60),
(2, 2, 20, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 40),
(3, 3, 20, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 35),
(4, 4, 20, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 35),
(5, 5, 15, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 45),
(6, 6, 10, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 50),
(7, 7, 15, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 45),
(8, 8, 30, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 25),
(9, 9, 20, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 35),
(10, 10, 30, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 30),
(11, 11, 30, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 30),
(12, 12, 30, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 20),
(13, 13, 20, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 25),
(14, 14, 10, '07:30:00', '17:00:00', '12:00:00', '13:00:00', 70);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Chỉ mục cho bảng `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `account_id` (`account_id`);

--
-- Chỉ mục cho bảng `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `doctor_id` (`doctor_id`),
  ADD KEY `fk_app_rule_v2` (`time_slot_rule_id`);

--
-- Chỉ mục cho bảng `chat_message`
--
ALTER TABLE `chat_message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_chat_account` (`account_id`);

--
-- Chỉ mục cho bảng `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_doctor_department` (`department_id`);

--
-- Chỉ mục cho bảng `email_verification`
--
ALTER TABLE `email_verification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `account_id` (`account_id`);

--
-- Chỉ mục cho bảng `email_verification_otp`
--
ALTER TABLE `email_verification_otp`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `account_id` (`account_id`);

--
-- Chỉ mục cho bảng `password_reset_otp`
--
ALTER TABLE `password_reset_otp`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `account_id` (`account_id`);

--
-- Chỉ mục cho bảng `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `transaction_ref` (`transaction_ref`),
  ADD UNIQUE KEY `appointment_id` (`appointment_id`);

--
-- Chỉ mục cho bảng `time_slot_rule`
--
ALTER TABLE `time_slot_rule`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_time_slot_rule_department` (`department_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT cho bảng `admin`
--
ALTER TABLE `admin`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `appointment`
--
ALTER TABLE `appointment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT cho bảng `chat_message`
--
ALTER TABLE `chat_message`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT cho bảng `department`
--
ALTER TABLE `department`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `doctor`
--
ALTER TABLE `doctor`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT cho bảng `email_verification`
--
ALTER TABLE `email_verification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `email_verification_otp`
--
ALTER TABLE `email_verification_otp`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `notification`
--
ALTER TABLE `notification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `password_reset_otp`
--
ALTER TABLE `password_reset_otp`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `patient`
--
ALTER TABLE `patient`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `payment`
--
ALTER TABLE `payment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT cho bảng `time_slot_rule`
--
ALTER TABLE `time_slot_rule`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

--
-- Các ràng buộc cho bảng `appointment`
--
ALTER TABLE `appointment`
  ADD CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
  ADD CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`),
  ADD CONSTRAINT `fk_app_rule` FOREIGN KEY (`time_slot_rule_id`) REFERENCES `time_slot_rule` (`id`),
  ADD CONSTRAINT `fk_app_rule_v2` FOREIGN KEY (`time_slot_rule_id`) REFERENCES `time_slot_rule` (`id`);

--
-- Các ràng buộc cho bảng `chat_message`
--
ALTER TABLE `chat_message`
  ADD CONSTRAINT `fk_chat_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

--
-- Các ràng buộc cho bảng `doctor`
--
ALTER TABLE `doctor`
  ADD CONSTRAINT `fk_doctor_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);

--
-- Các ràng buộc cho bảng `email_verification`
--
ALTER TABLE `email_verification`
  ADD CONSTRAINT `email_verification_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

--
-- Các ràng buộc cho bảng `patient`
--
ALTER TABLE `patient`
  ADD CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

--
-- Các ràng buộc cho bảng `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `fk_payment_appointment` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`);

--
-- Các ràng buộc cho bảng `time_slot_rule`
--
ALTER TABLE `time_slot_rule`
  ADD CONSTRAINT `fk_time_slot_rule_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
