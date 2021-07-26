/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 10.4.11-MariaDB : Database - db_travel
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_travel` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `db_travel`;

/*Table structure for table `tbl_customer` */

DROP TABLE IF EXISTS `tbl_customer`;

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
  `foto` varchar(100) NOT NULL,
  PRIMARY KEY (`no_ktp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `tbl_customer` */

insert  into `tbl_customer`(`no_ktp`,`nama`,`jns_kelamin`,`tgl_lahir`,`gol_darah`,`tempat_lahir`,`alamat`,`no_telp`,`email`,`foto`) values 
('111111','Nursalim','L','2021-07-20','AB','Brebes','asdsa','2312312','nursalim.me@gmail.com','test_photo.png'),
('1122334455','Naura Krasiva','P','2021-07-22','AB','Jakarta','Jalan Raya Bogor','383838','nursalim.me@gmail.com','test_photo.png');

/*Table structure for table `tbl_hotel` */

DROP TABLE IF EXISTS `tbl_hotel`;

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

/*Data for the table `tbl_hotel` */

insert  into `tbl_hotel`(`nama`,`lokasi`,`bintang`,`tarif`,`created_by`,`created_dt`,`updated_by`,`updated_dt`) values 
('Hotel Shangrilla','Mekah 123','Bintang 1',10000000,NULL,'2021-07-18 20:32:54',NULL,NULL),
('Hotel Melati','Jakarta','Bintang 1',100000,NULL,'2021-07-20 13:50:31',NULL,NULL);

/*Table structure for table `tbl_maskapai` */

DROP TABLE IF EXISTS `tbl_maskapai`;

CREATE TABLE `tbl_maskapai` (
  `nama` varchar(50) NOT NULL,
  `bandara` varchar(100) NOT NULL,
  `kelas` varchar(50) NOT NULL,
  `tarif` int(12) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_dt` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_dt` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`nama`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `tbl_maskapai` */

insert  into `tbl_maskapai`(`nama`,`bandara`,`kelas`,`tarif`,`created_by`,`created_dt`,`updated_by`,`updated_dt`) values 
('Citilink','Soekarno Hatta eut','Presiden Suite',10000000,NULL,'2021-07-20 13:30:35',NULL,NULL),
('Etihad Airways','King Abdul Aziz','Eksekutif',2000000,NULL,'2021-07-21 20:55:03',NULL,NULL),
('Test123','Test123','Bisnis',100000,NULL,'2021-07-21 21:03:24',NULL,NULL);

/*Table structure for table `tbl_paket_haji` */

DROP TABLE IF EXISTS `tbl_paket_haji`;

CREATE TABLE `tbl_paket_haji` (
  `nama_paket` varchar(50) NOT NULL,
  `hotel` varchar(50) NOT NULL,
  `maskapai` varchar(50) NOT NULL,
  `transportasi` varchar(50) NOT NULL,
  `fasilitas` varchar(255) NOT NULL,
  `harga` int(10) DEFAULT NULL,
  PRIMARY KEY (`nama_paket`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `tbl_paket_haji` */

insert  into `tbl_paket_haji`(`nama_paket`,`hotel`,`maskapai`,`transportasi`,`fasilitas`,`harga`) values 
('Paket Haji 1','Hotel Shangrilla','Garuda Indonesia','Travel Antar Jemput','Makan Minum, Buku Manasik, Batik, Air Zamzam, Kain Ihram',36000000),
('Paket Haji Mabrur','Hotel Melati','Citilink','Jet','asdsa',123456);

/*Table structure for table `tbl_paket_umrah` */

DROP TABLE IF EXISTS `tbl_paket_umrah`;

CREATE TABLE `tbl_paket_umrah` (
  `nama_paket` varchar(50) NOT NULL,
  `hotel` varchar(50) NOT NULL,
  `maskapai` varchar(50) NOT NULL,
  `transportasi` varchar(50) NOT NULL,
  `fasilitas` varchar(255) NOT NULL,
  `harga` int(10) DEFAULT NULL,
  PRIMARY KEY (`nama_paket`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `tbl_paket_umrah` */

insert  into `tbl_paket_umrah`(`nama_paket`,`hotel`,`maskapai`,`transportasi`,`fasilitas`,`harga`) values 
('Paket Hemat','Hotel Shangrilla','Citilink','Jet Pribadi','Kamar Mandi, Baju Ihram, Batik, Air Zamza',12345),
('Paket Umrah 1','Hotel Shangrilla','Garuda Indonesia','antar jemput di hotel','Batik, Buku Manasik, Baju Batik, Pakaian Ikhram',123456);

/*Table structure for table `tbl_pemesanan` */

DROP TABLE IF EXISTS `tbl_pemesanan`;

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
  `uang_dp` int(10) DEFAULT NULL,
  PRIMARY KEY (`no_pemesanan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `tbl_pemesanan` */

insert  into `tbl_pemesanan`(`no_pemesanan`,`no_ktp`,`nama_paket`,`jns_pembayaran`,`tgl_berangkat`,`tgl_pulang`,`tgl_pemesanan`,`status_pembayaran`,`no_registrasi`,`total_bayar`,`pimpinan_rombongan`,`tipe_pemesanan`,`uang_dp`) values 
('P210720LEA9','111111','Paket Haji Mabrur','Kredit','2021-09-01','2021-09-01','2021-07-20','Lunas','R210720GS02',123456,'Ust B','Haji',10000000),
('P2107223HRR','111111','Paket Haji 1','Tunai',NULL,NULL,'2021-07-22','Belum Lunas',NULL,36000000,NULL,'Haji',0),
('P210722U6LH','1122334455','Paket Umrah 1','Kredit',NULL,NULL,'2021-07-22','Belum Lunas',NULL,123456,NULL,'Umrah',900000),
('P210723VOZZ','1122334455','Paket Hemat','Tunai',NULL,NULL,'2021-07-23','Belum Lunas',NULL,12345,NULL,'Umrah',12345),
('P210723X42M','111111','Paket Umrah 1','Kredit',NULL,NULL,'2021-07-23','Belum Lunas',NULL,123456,NULL,'Umrah',100000),
('P210723XA1L','1122334455','Paket Haji 1','Kredit',NULL,NULL,'2021-07-23','Belum Lunas',NULL,36000000,NULL,'Haji',1000000);

/*Table structure for table `tbl_user` */

DROP TABLE IF EXISTS `tbl_user`;

CREATE TABLE `tbl_user` (
  `user_id` varchar(50) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `level` varchar(50) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_dt` datetime DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `tbl_user` */

insert  into `tbl_user`(`user_id`,`user_name`,`password`,`email`,`level`,`created_by`,`created_dt`,`updated_by`,`updated_dt`) values 
('admin','Administrator','AsALsEvRhpTwOWMaKl/o4g==','admin@gmail.com','Admin','','0000-00-00 00:00:00','','0000-00-00 00:00:00'),
('nursalim','Nursalim 123','5bwyqDN/Vr5h/Q6AJSKatA==','nursalim@gmail.com','Manager',NULL,'2021-07-20 00:00:00',NULL,NULL),
('test123','Test123','5bwyqDN/Vr5h/Q6AJSKatA==','nursalim.me@gmail.com','Admin',NULL,'2021-07-21 00:00:00',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
