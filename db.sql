-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Employee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Employee` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Employee` (
  `EmployeeID` VARCHAR(45) NOT NULL,
  `FullName` VARCHAR(45) NOT NULL,
  `Gender` VARCHAR(45) NULL,
  `DateOfBirth` DATE NOT NULL,
  `IdentificationType` VARCHAR(45) NOT NULL,
  `IdentificationNumber` VARCHAR(45) NOT NULL,
  `Citizenship` VARCHAR(45) NOT NULL,
  `Visa` VARCHAR(45) NOT NULL,
  `Address` VARCHAR(45) NOT NULL,
  `BankCardNumber` VARCHAR(45) NOT NULL,
  `EmailAddress` VARCHAR(45) NOT NULL,
  `HomePhoneNumber` VARCHAR(45) NULL,
  `MobilePhoneNumber` VARCHAR(45) NOT NULL,
  `Login` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`EmployeeID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Guest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Guest` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Guest` (
  `GuestID` VARCHAR(45) NOT NULL,
  `FullName` VARCHAR(45) NOT NULL,
  `IdentificationType` VARCHAR(45) NOT NULL,
  `IdentificationNumber` VARCHAR(45) NOT NULL,
  `Category` VARCHAR(45) NOT NULL,
  `Address` VARCHAR(45) NOT NULL,
  `HomePhoneNumber` VARCHAR(45) NULL,
  `MobilePhoneNumber` VARCHAR(45) NOT NULL,
  `Login` VARCHAR(45) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`GuestID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Hotel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Hotel` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Hotel` (
  `HotelID` VARCHAR(45) NOT NULL,
  `Name` VARCHAR(45) NOT NULL,
  `Country` VARCHAR(45) NOT NULL,
  `Region` VARCHAR(45) NOT NULL,
  `Address` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`HotelID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Employee_At_Hotel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Employee_At_Hotel` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Employee_At_Hotel` (
  `Employee_EmployeeID` VARCHAR(45) NOT NULL,
  `Hotel_HotelID` VARCHAR(45) NOT NULL,
  `Position` VARCHAR(45) NOT NULL,
  `Status` VARCHAR(45) NOT NULL,
  `PayRate` VARCHAR(45) NOT NULL,
  `StartDate` DATE NOT NULL,
  `EndDate` DATE NULL,
  PRIMARY KEY (`Hotel_HotelID`, `Employee_EmployeeID`),
  INDEX `fk_Employee_has_Hotel_Hotel1_idx` (`Hotel_HotelID` ASC) VISIBLE,
  INDEX `fk_Employee_has_Hotel_Employee_idx` (`Employee_EmployeeID` ASC) VISIBLE,
  CONSTRAINT `fk_Employee_has_Hotel_Employee`
    FOREIGN KEY (`Employee_EmployeeID`)
    REFERENCES `mydb`.`Employee` (`EmployeeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Employee_has_Hotel_Hotel1`
    FOREIGN KEY (`Hotel_HotelID`)
    REFERENCES `mydb`.`Hotel` (`HotelID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
