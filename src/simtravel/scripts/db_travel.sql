-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 09 Jul 2021 pada 16.13
-- Versi server: 10.4.8-MariaDB
-- Versi PHP: 7.3.10

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
-- Struktur dari tabel `tbl_customer`
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

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_hotel`
--

CREATE TABLE `tbl_hotel` (
  `nama` varchar(50) NOT NULL,
  `lokasi` varchar(50) NOT NULL,
  `bintang` int(1) NOT NULL,
  `tarif` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_maskapai`
--

CREATE TABLE `tbl_maskapai` (
  `nama` varchar(50) NOT NULL,
  `no_pesawat` varchar(50) NOT NULL,
  `bandara` varchar(100) NOT NULL,
  `kelas` varchar(50) NOT NULL,
  `tarif` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_paket_haji`
--

CREATE TABLE `tbl_paket_haji` (
  `nama_paket` varchar(50) NOT NULL,
  `hotel` varchar(50) NOT NULL,
  `maskapai` varchar(50) NOT NULL,
  `transportasi` varchar(50) NOT NULL,
  `fasilitas` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_paket_umrah`
--

CREATE TABLE `tbl_paket_umrah` (
  `nama_paket` varchar(50) NOT NULL,
  `hotel` varchar(50) NOT NULL,
  `maskapai` varchar(50) NOT NULL,
  `transportasi` varchar(50) NOT NULL,
  `fasilitas` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_pemesanan`
--

CREATE TABLE `tbl_pemesanan` (
  `no_pemesanan` varchar(100) NOT NULL,
  `no_ktp` varchar(20) NOT NULL,
  `nama_paket` varchar(50) NOT NULL,
  `jns_pembayaran` varchar(50) NOT NULL,
  `tgl_berangkat` date NOT NULL,
  `tgl_pulang` date NOT NULL,
  `tgl_pemesanan` date NOT NULL,
  `status_pembayaran` varchar(50) NOT NULL,
  `no_registrasi` varchar(50) NOT NULL,
  `total_bayar` int(11) NOT NULL,
  `pimpinan_rombongan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tbl_user`
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
-- Dumping data untuk tabel `tbl_user`
--

INSERT INTO `tbl_user` (`user_id`, `user_name`, `password`, `email`, `level`, `created_by`, `created_dt`, `updated_by`, `updated_dt`) VALUES
('admin', 'Administrator', 'AsALsEvRhpTwOWMaKl/o4g==', 'admin@gmail.com', 'Admin', '', '0000-00-00 00:00:00', '', '0000-00-00 00:00:00'),
('nursalim', 'Nursalim 123', 'S507auRD6gaC1qefj4aa4g==', 'nursalim.me@gmail.com', 'Admin', NULL, '2021-07-09 00:00:00', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tbl_customer`
--
ALTER TABLE `tbl_customer`
  ADD PRIMARY KEY (`no_ktp`);

--
-- Indeks untuk tabel `tbl_maskapai`
--
ALTER TABLE `tbl_maskapai`
  ADD PRIMARY KEY (`no_pesawat`);

--
-- Indeks untuk tabel `tbl_paket_haji`
--
ALTER TABLE `tbl_paket_haji`
  ADD PRIMARY KEY (`nama_paket`);

--
-- Indeks untuk tabel `tbl_paket_umrah`
--
ALTER TABLE `tbl_paket_umrah`
  ADD PRIMARY KEY (`nama_paket`);

--
-- Indeks untuk tabel `tbl_pemesanan`
--
ALTER TABLE `tbl_pemesanan`
  ADD PRIMARY KEY (`no_pemesanan`);

--
-- Indeks untuk tabel `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
