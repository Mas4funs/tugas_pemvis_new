-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 11, 2021 at 10:40 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_travel`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_customer`
--

CREATE TABLE `tbl_customer` (
  `no_ktp` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jns_kelamin` char(1) NOT NULL,
  `tgl_lahir` date NOT NULL,
  `gol_darah` char(2) NOT NULL,
  `tempat_lahir` varchar(100) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `no_telp` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `foto` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_customer`
--

INSERT INTO `tbl_customer` (`no_ktp`, `nama`, `jns_kelamin`, `tgl_lahir`, `gol_darah`, `tempat_lahir`, `alamat`, `no_telp`, `email`, `foto`) VALUES
('111111', 'Nursalim', 'L', '2021-07-20', 'AB', 'Brebes', 'asdsa', '2312312', '4funs.adi@gmail.com', ''),
('11111111', 'adi santoso', 'L', '2021-08-01', 'A', 'jakarta', 'jl. wkwkw', '081212', 'virgan1st@gmail.com', '1619107333638.png'),
('1122334455', 'Naura Krasiva', 'P', '2021-07-22', 'AB', 'Jakarta', 'Jalan Raya Bogor', '383838', '4funs.adi@gmail.com', 'test_photo.png'),
('1234', 'adi', 'L', '2021-08-02', 'A', 'akar', 'wew', '0812', 'virgan1st@gmail.com', 'test_photo.png');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_hotel`
--

CREATE TABLE `tbl_hotel` (
  `nama` varchar(50) NOT NULL,
  `lokasi` varchar(50) NOT NULL,
  `bintang` varchar(10) NOT NULL,
  `tarif` int(12) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_dt` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_dt` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_hotel`
--

INSERT INTO `tbl_hotel` (`nama`, `lokasi`, `bintang`, `tarif`, `created_by`, `created_dt`, `updated_by`, `updated_dt`) VALUES
('Hotel Shangrilla', 'Mekah 123', 'Bintang 1', 10000000, NULL, '2021-07-18 13:32:54', NULL, NULL),
('Hotel Melati', 'Jakarta', 'Bintang 1', 100000, NULL, '2021-07-20 06:50:31', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_maskapai`
--

CREATE TABLE `tbl_maskapai` (
  `nama` varchar(50) NOT NULL,
  `bandara` varchar(100) NOT NULL,
  `kelas` varchar(50) NOT NULL,
  `tarif` int(12) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_dt` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_dt` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_maskapai`
--

INSERT INTO `tbl_maskapai` (`nama`, `bandara`, `kelas`, `tarif`, `created_by`, `created_dt`, `updated_by`, `updated_dt`) VALUES
('Citilink', 'Soekarno Hatta eut', 'Presiden Suite', 10000000, NULL, '2021-07-20 06:30:35', NULL, NULL),
('Etihad Airways', 'King Abdul Aziz', 'Eksekutif', 2000000, NULL, '2021-07-21 13:55:03', NULL, NULL),
('Test123', 'Test123', 'Bisnis', 100000, NULL, '2021-07-21 14:03:24', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_paket_haji`
--

CREATE TABLE `tbl_paket_haji` (
  `nama_paket` varchar(50) NOT NULL,
  `hotel` varchar(50) NOT NULL,
  `maskapai` varchar(50) NOT NULL,
  `transportasi` varchar(50) NOT NULL,
  `fasilitas` varchar(255) NOT NULL,
  `harga` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_paket_haji`
--

INSERT INTO `tbl_paket_haji` (`nama_paket`, `hotel`, `maskapai`, `transportasi`, `fasilitas`, `harga`) VALUES
('Paket Haji 1', 'Hotel Shangrilla', 'Citilink', 'Travel Antar Jemput', 'Makan Minum, Buku Manasik, Batik, Air Zamzam, Kain Ihram', 36000000),
('Paket Haji Mabrur', 'Hotel Melati', 'Citilink', 'Jet', 'asdsa', 123456);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_paket_umrah`
--

CREATE TABLE `tbl_paket_umrah` (
  `nama_paket` varchar(50) NOT NULL,
  `hotel` varchar(50) NOT NULL,
  `maskapai` varchar(50) NOT NULL,
  `transportasi` varchar(50) NOT NULL,
  `fasilitas` varchar(255) NOT NULL,
  `harga` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_paket_umrah`
--

INSERT INTO `tbl_paket_umrah` (`nama_paket`, `hotel`, `maskapai`, `transportasi`, `fasilitas`, `harga`) VALUES
('Paket Hemat', 'Hotel Shangrilla', 'Citilink', 'Jet Pribadi', 'Kamar Mandi, Baju Ihram, Batik, Air Zamza', 12345),
('Paket Umrah 1', 'Hotel Shangrilla', 'Garuda Indonesia', 'antar jemput di hotel', 'Batik, Buku Manasik, Baju Batik, Pakaian Ikhram', 123456);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pemesanan`
--

CREATE TABLE `tbl_pemesanan` (
  `no_pemesanan` varchar(100) NOT NULL,
  `no_ktp` varchar(20) DEFAULT NULL,
  `nama_paket` varchar(50) DEFAULT NULL,
  `jns_pembayaran` varchar(50) DEFAULT NULL,
  `tgl_berangkat` date DEFAULT NULL,
  `tgl_pulang` date DEFAULT NULL,
  `tgl_pemesanan` date DEFAULT NULL,
  `status_pembayaran` varchar(50) DEFAULT NULL,
  `no_registrasi` varchar(50) DEFAULT NULL,
  `total_bayar` int(11) DEFAULT NULL,
  `pimpinan_rombongan` varchar(50) DEFAULT NULL,
  `tipe_pemesanan` varchar(10) DEFAULT NULL,
  `uang_dp` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_pemesanan`
--

INSERT INTO `tbl_pemesanan` (`no_pemesanan`, `no_ktp`, `nama_paket`, `jns_pembayaran`, `tgl_berangkat`, `tgl_pulang`, `tgl_pemesanan`, `status_pembayaran`, `no_registrasi`, `total_bayar`, `pimpinan_rombongan`, `tipe_pemesanan`, `uang_dp`) VALUES
('P210720LEA9', '111111', 'Paket Haji Mabrur', 'Kredit', '2021-08-01', '2021-08-07', '2021-01-20', 'Lunas', 'R210720GS02', 123456, 'Ust B', 'Haji', 10000000),
('P2107223HRR', '111111', 'Paket Haji 1', 'Tunai', '2021-08-03', '2021-08-10', '2021-04-22', 'Lunas', 'R210806EKBK', 36000000, 'Ust B', 'Haji', 0),
('P210722U6LH', '1122334455', 'Paket Umrah 1', 'Kredit', '2021-08-08', '2021-08-08', '2021-08-22', 'Lunas', 'R210807HQ77', 123456, 'Ust C', 'Umrah', 900000),
('P210723XA1L', '1122334455', 'Paket Haji 1', 'Kredit', NULL, NULL, '2021-12-23', 'Belum Lunas', NULL, 36000000, NULL, 'Haji', 1000000),
('P21080732KD', '1234', 'Paket Hemat', 'Tunai', '2021-08-17', '2021-09-17', '2021-08-07', 'Lunas', 'R210810YEHO', 12345, 'Ust C', 'Umrah', 0),
('P210807KPAS', '111111', 'Paket Haji 1', 'Kredit', NULL, NULL, '2021-01-07', 'Belum Lunas', NULL, 36000000, NULL, 'Haji', 1000000),
('P210807S2P8', '11111111', 'Paket Haji 1', 'Kredit', NULL, NULL, '2021-09-07', 'Belum Lunas', NULL, 36000000, NULL, 'Haji', 1000000),
('P210807S652', '1234', 'Paket Haji 1', 'Tunai', '2021-08-01', '2021-08-31', '2021-10-07', 'Lunas', 'R210810Z822', 36000000, 'Ust A', 'Haji', 0),
('P210807YM8O', '1234', 'Paket Haji 1', 'Tunai', '2021-08-01', '2021-08-31', '2021-10-07', 'Lunas', 'R210810YOON', 36000000, 'Ust A', 'Haji', 1000000),
('P210809KRNU', '1234', 'Paket Hemat', 'Tunai', '2021-08-01', '2021-08-31', '2021-08-09', 'Lunas', 'R210809FP23', 12345, 'Ust A', 'Umrah', 0),
('P210810CA6W', '1234', 'Paket Haji 1', 'Kredit', '2021-08-11', '2021-08-31', '2021-08-10', 'Lunas', 'R2108100HVW', 36000000, 'Ust B', 'Haji', 1000000),
('P210810VANE', '1234', 'Paket Haji 1', 'Kredit', '2021-08-31', '2021-08-31', '2021-08-10', 'Lunas', 'R2108109SBJ', 36000000, 'Ust B', 'Haji', 1000);

-- --------------------------------------------------------
--
-- Table structure for table `tbl_trasnport`
--

CREATE TABLE `tbl_transport` (
  `nama` varchar(50) NOT NULL,
  `kelas` varchar(100) NOT NULL,
  `status` varchar(50) NOT NULL,
  `tarif` int(12) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_dt` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_dt` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transport`
--

INSERT INTO `tbl_transport` (`nama`, `kelas`, `status`, `tarif`, `created_by`, `created_dt`, `updated_by`, `updated_dt`) VALUES
('Bus', 'Soekarno Hatta eut', 'Presiden Suite', 10000000, NULL, '2021-07-20 06:30:35', NULL, NULL),
('Taxi', 'King Abdul Aziz', 'Eksekutif', 2000000, NULL, '2021-07-21 13:55:03', NULL, NULL),
('Motor', 'Test123', 'Bisnis', 100000, NULL, '2021-07-21 14:03:24', NULL, NULL);

-- --------------------------------------------------------
--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `user_id` varchar(50) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `level` varchar(50) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`user_id`, `user_name`, `password`, `email`, `level`, `created_by`, `created_dt`, `updated_by`, `updated_dt`) VALUES
('admin', 'Administrator', 'AsALsEvRhpTwOWMaKl/o4g==', 'admin@gmail.com', 'Admin', '', '0000-00-00 00:00:00', '', '0000-00-00 00:00:00'),
('nursalim', 'Nursalim 123', '5bwyqDN/Vr5h/Q6AJSKatA==', 'nursalim@gmail.com', 'Manager', NULL, '2021-07-20 00:00:00', NULL, NULL),
('test123', 'Test123', '5bwyqDN/Vr5h/Q6AJSKatA==', 'nursalim.me@gmail.com', 'Admin', NULL, '2021-07-21 00:00:00', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_customer`
--
ALTER TABLE `tbl_customer`
  ADD PRIMARY KEY (`no_ktp`);

--
-- Indexes for table `tbl_maskapai`
--
ALTER TABLE `tbl_maskapai`
  ADD PRIMARY KEY (`nama`);

--
-- Indexes for table `tbl_paket_haji`
--
ALTER TABLE `tbl_paket_haji`
  ADD PRIMARY KEY (`nama_paket`);

--
-- Indexes for table `tbl_paket_umrah`
--
ALTER TABLE `tbl_paket_umrah`
  ADD PRIMARY KEY (`nama_paket`);

--
-- Indexes for table `tbl_pemesanan`
--
ALTER TABLE `tbl_pemesanan`
  ADD PRIMARY KEY (`no_pemesanan`);

--
-- Indexes for table `tbl_transport`
--
ALTER TABLE `tbl_transport`
  ADD PRIMARY KEY (`nama`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
