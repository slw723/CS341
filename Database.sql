CREATE TABLE Appointment (
  ApptId INT PRIMARY KEY NOT NULL AUTOINCREMENT,
  Type NVARCHAR(8) NOT NULL,
  Time TIME NOT NULL,
  Day NVARCHAR(10) NOT NULL,
  Length INT NOT NULL,
  Booked NVARCHAR(3) NULL,
  );

CREATE TABLE User (
  FirstName NVARCHAR(12) NOT NULL,
  LastName NVARCHAR(12) NOT NULL,
  Email NVARCHAR(25) PRIMARY KEY NOT NULL,
  Password VARCHAR(255) NOT NULL,
  PhoneNum VARCHAR(8) NOT NULL,
  );

CREATE TABLE ServiceProvider (
  FirstName NVARCHAR(12) NOT NULL,
  LastName NVARCHAR(12) NOT NULL,
  Email NVARCHAR(25) PRIMARY KEY NOT NULL,
  Password VARCHAR(255) NOT NULL,
  PhoneNum INT NOT NULL,
  StartTime INT NOT NULL,
  StopTime INT NOT NULL,
  Qualification NVARCHAR(120) NOT NULL,
  YearGraduated INT NOT NULL,
  Type NVARCHAR(8) NOT NULL,
  );

CREATE TABLE Admin (
  UserId NVARCHAR(12) PRIMARY KEY NOT NULL,
  Password VARCHAR(255) NOT NULL,
  );

CREATE TABLE Has(
  CONSTRAINT fk_user_email FOREIGN KEY (User) REFERENCES User(Email),
  CONSTRAINT fk_appt_id FOREIGN KEY (Appointment) REFERENCES Appointment(ApptId)
)

CREATE TABLE Owns(
  CONSTRAINT fk_sp_email FOREIGN KEY (ServiceProvider) REFERENCES ServiceProvider(Email),
  CONSTRAINT fk_appt_id FOREIGN KEY (Appointment) REFERENCES Appointment(ApptId)
)