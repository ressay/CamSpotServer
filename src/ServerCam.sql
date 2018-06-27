-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 26, 2018 at 04:04 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-1+ubuntu16.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ServerCam`
--

-- --------------------------------------------------------

--
-- Table structure for table `Account`
--

CREATE TABLE `Account` (
  `idAccount` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `phoneNumber` tinyint(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Account`
--

INSERT INTO `Account` (`idAccount`, `email`, `name`, `password`, `phoneNumber`) VALUES
(1, 'unknown@unknown.unknown', 'unknown', 'unknown', 64);

-- --------------------------------------------------------

--
-- Table structure for table `Frame_problems`
--

CREATE TABLE `Frame_problems` (
  `idFrame_problems` int(11) NOT NULL,
  `problem_description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Frame_received`
--

CREATE TABLE `Frame_received` (
  `idFrame` int(11) NOT NULL,
  `ipAddress` int(11) NOT NULL COMMENT 'this table is related to the "received frames from our devices"\nnote:\nip address is a int .( we have to use INET_ATON and INET_NTOA to convert them while inserting)\nreference:stackoverflow\n\n',
  `MetaDataDescription` json NOT NULL COMMENT 'MetadataDescription:\nis a JSON FILE (since the datatype existed i put it )\n',
  `Image_url` varchar(255) NOT NULL COMMENT 'image_url is supposed to be the frame ( one frame ) that we are collecting from cameras on cars .\n',
  `Account_idAccount` int(11) NOT NULL,
  `Frame_receivedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Spotted_anomaly_idSpotted_anomaly` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=dec8;

--
-- Dumping data for table `Frame_received`
--

INSERT INTO `Frame_received` (`idFrame`, `ipAddress`, `MetaDataDescription`, `Image_url`, `Account_idAccount`, `Frame_receivedDate`, `Spotted_anomaly_idSpotted_anomaly`) VALUES
(2, 2130706433, '{"lat": 3.456123, "lon": 1.556123}', '/home/masterubunto/Pictures/screen1.jpg', 1, '2018-06-25 13:49:02', NULL),
(3, 2130706434, '{"lat": 3.456123, "lon": 1.556123}', '/home/masterubunto/Pictures/screen2.jpg', 1, '2018-06-25 13:52:37', NULL),
(4, 2130706434, '{"lat": 3.456123, "lon": 1.556123}', '/home/masterubunto/Pictures/screen3.jpg', 1, '2018-06-25 14:28:18', NULL),
(5, 2130706434, '{"lat": 3.456123, "lon": 1.556123}', '/home/masterubunto/Pictures/screen4.jpg', 1, '2018-06-25 14:58:28', NULL),
(6, 2130706434, '{"lat": 3.456123, "lon": 1.556123}', '/home/masterubunto/Pictures/screen4.jpg', 1, '2018-06-25 14:59:27', NULL),
(7, 2130706434, '{"lat": 3.456123, "lon": 1.556123}', '/home/masterubunto/Pictures/screen4.jpg', 1, '2018-06-25 14:59:53', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Frame_received_has_Frame_problems`
--

CREATE TABLE `Frame_received_has_Frame_problems` (
  `Frame_received_idFrame` int(11) NOT NULL,
  `Frame_problems_idFrame_problems` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Spotted_anomalie_has_Spotted_anomalie`
--

CREATE TABLE `Spotted_anomalie_has_Spotted_anomalie` (
  `Spotted_anomalie_idSpotted_anomalie` int(11) NOT NULL,
  `Spotted_anomalie_idSpotted_anomalie1` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Spotted_anomaly`
--

CREATE TABLE `Spotted_anomaly` (
  `idSpotted_anomaly` int(11) NOT NULL,
  `Anomaly_description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Account`
--
ALTER TABLE `Account`
  ADD PRIMARY KEY (`idAccount`),
  ADD UNIQUE KEY `email_UNIQUE` (`email`);

--
-- Indexes for table `Frame_problems`
--
ALTER TABLE `Frame_problems`
  ADD PRIMARY KEY (`idFrame_problems`);

--
-- Indexes for table `Frame_received`
--
ALTER TABLE `Frame_received`
  ADD PRIMARY KEY (`idFrame`),
  ADD KEY `fk_Frame_received_Account_idx` (`Account_idAccount`),
  ADD KEY `fk_Frame_received_Spotted_anomaly1_idx` (`Spotted_anomaly_idSpotted_anomaly`);

--
-- Indexes for table `Frame_received_has_Frame_problems`
--
ALTER TABLE `Frame_received_has_Frame_problems`
  ADD PRIMARY KEY (`Frame_received_idFrame`,`Frame_problems_idFrame_problems`),
  ADD KEY `fk_Frame_received_has_Frame_problems_Frame_problems1_idx` (`Frame_problems_idFrame_problems`),
  ADD KEY `fk_Frame_received_has_Frame_problems_Frame_received1_idx` (`Frame_received_idFrame`);

--
-- Indexes for table `Spotted_anomalie_has_Spotted_anomalie`
--
ALTER TABLE `Spotted_anomalie_has_Spotted_anomalie`
  ADD PRIMARY KEY (`Spotted_anomalie_idSpotted_anomalie`,`Spotted_anomalie_idSpotted_anomalie1`),
  ADD KEY `fk_Spotted_anomalie_has_Spotted_anomalie_Spotted_anomalie2_idx` (`Spotted_anomalie_idSpotted_anomalie1`),
  ADD KEY `fk_Spotted_anomalie_has_Spotted_anomalie_Spotted_anomalie1_idx` (`Spotted_anomalie_idSpotted_anomalie`);

--
-- Indexes for table `Spotted_anomaly`
--
ALTER TABLE `Spotted_anomaly`
  ADD PRIMARY KEY (`idSpotted_anomaly`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Account`
--
ALTER TABLE `Account`
  MODIFY `idAccount` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Frame_received`
--
ALTER TABLE `Frame_received`
  MODIFY `idFrame` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `Spotted_anomaly`
--
ALTER TABLE `Spotted_anomaly`
  MODIFY `idSpotted_anomaly` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `Frame_received`
--
ALTER TABLE `Frame_received`
  ADD CONSTRAINT `fk_Frame_received_Account` FOREIGN KEY (`Account_idAccount`) REFERENCES `Account` (`idAccount`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Frame_received_Spotted_anomaly1` FOREIGN KEY (`Spotted_anomaly_idSpotted_anomaly`) REFERENCES `Spotted_anomaly` (`idSpotted_anomaly`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Frame_received_has_Frame_problems`
--
ALTER TABLE `Frame_received_has_Frame_problems`
  ADD CONSTRAINT `fk_Frame_received_has_Frame_problems_Frame_problems1` FOREIGN KEY (`Frame_problems_idFrame_problems`) REFERENCES `Frame_problems` (`idFrame_problems`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Frame_received_has_Frame_problems_Frame_received1` FOREIGN KEY (`Frame_received_idFrame`) REFERENCES `Frame_received` (`idFrame`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `Spotted_anomalie_has_Spotted_anomalie`
--
ALTER TABLE `Spotted_anomalie_has_Spotted_anomalie`
  ADD CONSTRAINT `fk_Spotted_anomalie_has_Spotted_anomalie_Spotted_anomalie1` FOREIGN KEY (`Spotted_anomalie_idSpotted_anomalie`) REFERENCES `Spotted_anomaly` (`idSpotted_anomaly`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Spotted_anomalie_has_Spotted_anomalie_Spotted_anomalie2` FOREIGN KEY (`Spotted_anomalie_idSpotted_anomalie1`) REFERENCES `Spotted_anomaly` (`idSpotted_anomaly`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
