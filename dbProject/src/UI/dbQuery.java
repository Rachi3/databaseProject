package UI;

public class dbQuery {
    String[] dropTables = {"DROP TABLE IF EXISTS Tickets;",
            "DROP TABLE IF EXISTS Seats;",
            "DROP TABLE IF EXISTS ScreeningInfo;",
            "DROP TABLE IF EXISTS BookInfo;",
            "DROP TABLE IF EXISTS CustomersInfo;",
            "DROP TABLE IF EXISTS Movies;",
            "DROP TABLE IF EXISTS Screen;"};

    String[] tableNames = {"Movies",
            "Screen",
            "ScreeningInfo",
            "BookInfo",
            "Seats",
            "Tickets",
            "CustomersInfo"
    };

    String createMoviesTable = "CREATE TABLE Movies (" +
            "MovieID INT AUTO_INCREMENT PRIMARY KEY," +
            "MovieTitle VARCHAR(31) NOT NULL," +
            "RunningTime INT NOT NULL," +
            "FilmRating ENUM('G','PG','PG-13','PG-15','R') NOT NULL," +
            "Director VARCHAR(30) NOT NULL," +
            "Actors TEXT NOT NULL," +
            "Genre VARCHAR(20) NOT NULL," +
            "MovieDescription TEXT NOT NULL," +
            "ReleaseDate DATE NOT NULL," +
            "MovieRating DECIMAL(4,2) NOT NULL" +
            ");";

    String createScreenTable = "CREATE TABLE Screen(" +
            "ScreenID INT AUTO_INCREMENT PRIMARY KEY," +
            "IsActive BOOLEAN NOT NULL DEFAULT true," +
            "TotalSeats INT NOT NULL," +
            "ScreenRows INT NOT NULL," +
            "ScreenColumns INT NOT NULL" +
            ");";

    String createScreeningInfoTable = "CREATE TABLE ScreeningInfo (" +
            "ScreeningID INT AUTO_INCREMENT PRIMARY KEY," +
            "MovieID INT," +
            "ScreenID INT," +
            "ShowDate DATE NOT NULL," +
            "ScreeningDay VARCHAR(9)," +
            "NumberOfScreening INT NOT NULL," +
            "StartTime TIME NOT NULL," +
            "EndTime TIME NOT NULL," +
            "FOREIGN KEY (MovieID) REFERENCES Movies(MovieID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    String createSeatsTable = "CREATE TABLE Seats(" +
            "SeatID INT AUTO_INCREMENT PRIMARY KEY," +
            "ScreenID INT NOT NULL," +
            "SeatRow INT NOT NULL," +
            "SeatColumn INT NOT NULL," +
            "FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    String createCustomersInfoTable = "CREATE TABLE CustomersInfo(" +
            "CustomerID VARCHAR(256) PRIMARY KEY NOT NULL," +
            "CustomerName VARCHAR(50)," +
            "PhoneNum CHAR(11)," +
            "Email VARCHAR(50)" +
            ");";

    String createBookInfoTable = "CREATE TABLE BookInfo(" +
            "BookingNum INT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
            "PaymentMethod ENUM('현금','카드') NOT NULL," +
            "PaymentStatus ENUM('Pending', 'Completed') NOT NULL," +
            "CustomerID VARCHAR(256) NOT NULL," +
            "PaymentDate DATE NOT NULL," +
            "FOREIGN KEY (CustomerID) REFERENCES CustomersInfo(CustomerID) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    String createTicketsTable = "CREATE TABLE Tickets(" +
            "TicketID INT AUTO_INCREMENT PRIMARY KEY," +
            "ScreeningID INT NOT NULL," +
            "ScreenID INT NOT NULL," +
            "SeatID INT NOT NULL," +
            "BookingNum INT NOT NULL," +
            "TicketInsurance BOOLEAN NOT NULL DEFAULT FALSE," +
            "StandardPrice INT NOT NULL," +
            "SellingPrice INT NOT NULL," +
            "FOREIGN KEY (BookingNum) REFERENCES BookInfo(BookingNum) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (ScreeningID) REFERENCES ScreeningInfo(ScreeningID) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY (SeatID) REFERENCES Seats(SeatID) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    
  
    String insertMovies="INSERT INTO Movies (MovieTitle, RunningTime, FilmRating, Director, Actors, Genre, MovieDescription, ReleaseDate, MovieRating) VALUES"+
    		"('노인을 위한 나라는 없다.', 122, 'R', '코엔 형제', '배토미 리 존스, 하비에르 바르뎀', '스릴러', '코맥 매카시의 동명 소설을 원작으로 한 영화로, 범죄와 폭력을 중심으로 인간의 본성과 도덕적 혼란을 그린 스릴러입니다.', '2024-01-01', 9.0),"+
    		"('조디악', 157, 'R', '데이비드 핀처', '제이크 질렌할, 로버트 다우니 주니어', '미스터리', '미국의 연쇄살인마 조디악 킬러에 관한 데이비드 핀처의 영화로 실화를 충실히 다뤘다.', '2024-02-04', 8.0),"+
    		"('화양연화', 99, 'PG', '양가위', '양조위, 장만옥', '로맨스', '영화의 제목 화양연화(花樣年華)는 인생에서 꽃과 같이 가장 아름답고 행복한 시간을 의미한다.', '2024-03-03', 9.5),"+
    		"('월-E', 98, 'G', '앤드루 스탠튼', '벤 버트, 엘리사 나이트', '애니메이션', '쓰레기 더미에 파묻힌 지구에 홀로 남겨진 청소 로봇 월-E와 식물을 탐색하기 위해 지구에 온 탐사 로봇 이브와의 사랑을 다룬다.', '2024-04-07', 8.5),"+
    		"('올드보이', 120, 'R', '박찬욱', '최민식, 유지태', '복수', '동명의 만화를 원안으로 한 박찬욱 감독, 최민식, 유지태, 강혜정 주연의 스릴러 느와르 영화.', '2024-05-09', 9.0),"+
    		"('보헤미안 랩소디', 134, 'PG-13', '브라이언 싱어', '라미 말렉, 루시 보인턴', '전기', '전설적인 록 그룹 퀸의 보컬리스트 프레디 머큐리의 전기 영화다.', '2024-06-12', 8.0),"+
    		"('그린북', 130, 'PG-13', '피터 패럴리', '비고 모텐슨, 마허샬라 알리', '코미디', '비고 모텐슨과 마허샬라 알리가 주연을 맡은 피터 패럴리 감독의 로드 무비.', '2024-07-15', 7.5),"+
    		"('1917', 119, 'R', '샘 멘데스', '조지 맥케이, 딘-찰스 채프먼', '전쟁', '제1차 세계대전 중 소식을 전달하기 위한 두 병사의 여정', '2024-08-19', 8.3),"+
    		"('조커', 122, 'R', '토드 필립스', '호아킨 피닉스', '드라마', '사회로부터 버림받은 한 남자가 조커가 되어가는 과정을 그린 영화', '2024-09-02', 8.5),"+
    		"('어벤져스: 엔드게임', 181, 'PG-13', '안소니 루소, 조 루소', '로버트 다우니 주니어, 크리스 에반스', '액션', '어벤져스 팀이 타노스를 상대로 최후의 전투를 벌이는 이야기', '2024-10-24', 8.4),"+
    		"('덩케르트', 106, 'PG-13', '크리스토퍼 놀란', '톰 하디, 마크 라이런스', '전쟁', '제2차 세계대전 중 덩케르크에서의 대피 작전을 그린 영화', '2024-11-20', 7.9),"+
    		"('컨저링', 112, 'R', '제임스 완', '베라 파미가, 패트릭 윌슨', '공포', '실화를 바탕으로 한 페론 가족의 초자연적 현상을 조사하는 이야기', '2024-12-10', 7.5);";

    String insertScreen="INSERT INTO Screen (IsActive, TotalSeats, ScreenRows, ScreenColumns) VALUES"+
    		"(true, 12, 4, 3),"+  
    		"(true, 15, 3, 5),"+
    		"(true, 20, 4, 5),"+  
    		"(true, 25, 5, 5),"+ 
    		"(true, 12, 3, 4),"+ 
    		"(true, 10, 5, 2),"+  
    		"(true, 16, 4, 4),"+  
    		"(true, 16, 4, 4),"+ 
    		"(true, 12, 3, 4),"+ 
    		"(true, 15, 3, 5)," + 
    		"(true, 12, 4, 3),"+ 
    		"(true, 15, 5, 3);";
    
    String insertScreeningInfo="INSERT INTO ScreeningInfo (MovieID, ScreenID, ShowDate, ScreeningDay, NumberOfScreening, StartTime, EndTime) VALUES"+
    		"(1, 1, '2024-01-01', 'Monday', 1, '14:00:00', '16:02:00'),"+
    		"(2, 2, '2024-02-04', 'Sunday', 1, '17:00:00', '19:37:00'),"+
    		"(3, 3, '2024-03-03', 'Sunday', 1, '20:00:00', '21:39:00'),"+
    		"(4, 4, '2024-04-07', 'Sunday', 1, '12:00:00', '13:38:00'),"+
    		"(5, 5, '2024-05-09', 'Thursday', 1, '14:00:00', '16:00:00'),"+
    		"(6, 6, '2024-06-12', 'Wednesday', 1, '17:00:00', '19:14:00'),"+
    		"(7, 7, '2024-07-15', 'Monday', 1, '14:00:00', '16:10:00'),"+
    		"(8, 8, '2024-08-19', 'Monday', 1, '17:00:00', '18:59:00'),"+
    		"(9, 10, '2024-09-02', 'Monday', 1, '20:00:00', '22:02:00'),"+
    		"(10, 11, '2024-10-24', 'Thursday', 1, '12:00:00', '15:01:00'),"+
    		"(11, 12, '2024-11-20', 'Wednesday', 1, '16:00:00', '17:46:00'),"+
    		"(12, 9, '2024-12-10', 'Tuesday', 1, '18:00:00', '19:52:00'),"+
    		"(1, 2, '2024-01-01', 'Monday', 2, '17:00:00', '19:02:00'),"+
    		"(2, 3, '2024-02-04', 'Sunday', 2, '20:00:00', '22:37:00'),"+
    		"(3, 4, '2024-03-03', 'Sunday', 2, '23:00:00', '00:39:00'),"+
    		"(4, 5, '2024-04-07', 'Sunday', 2, '15:00:00', '16:38:00'),"+
    		"(5, 6, '2024-05-09', 'Thursday', 2, '17:00:00', '19:00:00'),"+
    		"(6, 7, '2024-06-12', 'Wednesday', 2, '20:00:00', '22:14:00'),"+
    		"(7, 8, '2024-07-15', 'Monday', 2, '17:00:00', '19:10:00'),"+
    		"(8, 9, '2024-08-19', 'Monday', 2, '20:00:00', '21:59:00'),"+
    		"(9, 11, '2024-09-02', 'Monday', 2, '23:00:00', '01:02:00'),"+
    		"(10, 12, '2024-10-24', 'Thursday', 2, '16:00:00', '19:01:00'),"+
    		"(11, 1, '2024-11-20', 'Wednesday', 2, '19:00:00', '20:46:00'),"+
    		"(12, 2, '2024-12-10', 'Tuesday', 2, '21:00:00', '22:52:00'),"+
    		"(1, 3, '2024-01-20', 'Saturday', 3, '14:00:00', '16:02:00'),"+
    		"(2, 4, '2024-02-18', 'Sunday', 3, '17:00:00', '19:37:00'),"+
    		"(3, 5, '2024-03-15', 'Friday', 3, '20:00:00', '21:39:00'),"+
    		"(4, 6, '2024-04-21', 'Sunday', 3, '12:00:00', '13:38:00'),"+
    		"(5, 7, '2024-05-23', 'Thursday', 3, '14:00:00', '16:00:00'),"+
    		"(6, 8, '2024-06-25', 'Tuesday', 3, '17:00:00', '19:14:00'),"+
    		"(7, 9, '2024-07-22', 'Monday', 3, '14:00:00', '16:10:00'),"+
    		"(8, 10, '2024-08-12', 'Monday', 3, '17:00:00', '18:59:00'),"+
    		"(9, 12, '2024-09-16', 'Monday', 3, '20:00:00', '22:02:00'),"+
    		"(10, 1, '2024-10-31', 'Thursday', 3, '12:00:00', '15:01:00'),"+
    		"(11, 2, '2024-11-27', 'Wednesday', 3, '16:00:00', '17:46:00'),"+
    		"(12, 3, '2024-12-24', 'Tuesday', 3, '18:00:00', '19:52:00');";

    
    String insertCustomerInfo="INSERT INTO CustomersInfo (CustomerID, CustomerName, PhoneNum, Email) VALUES"+
    		"('CUST001', '김철수', '01012345678', 'chulsoo@example.com'),"+
    		"('CUST002', '이영희', '01023456789', 'younghi@example.com'),"+
    		"('CUST003', '박지민', '01034567890', 'jimin@example.com'),"+
    		"('CUST004', '정바울', '01045678901', 'paul@example.com'),"+
    		"('CUST005', '손흥민', '01056789012', 'sonny@example.com'),"+
    		"('CUST006', '김연아', '01067890123', 'yuna@example.com'),"+
    		"('CUST007', '방탄소년단', '01078901234', 'bts@example.com'),"+
    		"('CUST008', '블랙핑크', '01089012345', 'blackpink@example.com'),"+
    		"('CUST009', '봉준호', '01090123456', 'bong@example.com'),"+
    		"('CUST010', '이수만', '01001234567', 'sooman@example.com'),"+
    		"('CUST011', '홍길동', '01012345000', 'gildong@example.com'),"+
    		"('CUST012', '고길동', '01023456000', 'gildong2@example.com'),"+
    		"('user1','김현우','01011332244','user1@example.com');";


    String insertBookInfo = "INSERT INTO BookInfo (PaymentMethod, PaymentStatus, CustomerID, PaymentDate) VALUES"+
    	    "('카드', 'Completed', 'CUST001', '2024-01-01'),"+
    	    "('현금', 'Completed', 'CUST002', '2024-01-02'),"+
    	    "('카드', 'Pending', 'CUST003', '2024-01-03'),"+
    	    "('카드', 'Completed', 'CUST004', '2024-01-04'),"+
    	    "('현금', 'Completed', 'CUST005', '2024-01-05'),"+
    	    "('카드', 'Pending', 'CUST006', '2024-01-06'),"+
    	    "('현금', 'Completed', 'CUST007', '2024-01-07'),"+
    	    "('카드', 'Completed', 'CUST008', '2024-01-08'),"+
    	    "('현금', 'Pending', 'CUST009', '2024-01-09'),"+
    	    "('카드', 'Completed', 'CUST010', '2024-01-10'),"+
    	    "('현금', 'Completed', 'CUST011', '2024-01-11'),"+
    	    "('카드', 'Pending', 'CUST012', '2024-01-12'),"+
    	    "('현금','Completed','user1','2024-02-01');";
    
    String insertTickets="INSERT INTO Tickets (ScreeningID, ScreenID, SeatID, BookingNum, TicketInsurance, StandardPrice, SellingPrice) VALUES"+
    		"(1,1,1,1,true,14000,12000),"+
    		"(2,2,13,2,true,14000,12000),"+
    		"(3,3,28,3,true,14000,12000),"+
    		"(4,4,48,4,true,14000,12000),"+
    		"(5,5,73,5,true,14000,12000),"+
    		"(6,6,85,6,true,14000,12000),"+
    		"(7,7,95,7,true,14000,12000),"+
    		"(8,8,111,8,true,14000,12000),"+
    		"(9,9,127,9,true,14000,12000),"+
    		"(10,10,139,10,true,14000,12000),"+
    		"(11,11,154,11,true,14000,12000),"+
    		"(12,12,166,12,true,14000,12000),"+
    		"(1,1,2,13,true,14000,12000);";
    
   
}
