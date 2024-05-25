package dbProject.UI;

public class dbQuery {
	String []dropTables ={"DROP TABLE IF EXISTS Tickets;" ,
            "DROP TABLE IF EXISTS Seats;" ,
            "DROP TABLE IF EXISTS ScreeningInfo;" ,
            "DROP TABLE IF EXISTS BookInfo;" ,
            "DROP TABLE IF EXISTS CustomersInfo;" ,
            "DROP TABLE IF EXISTS Movies;" ,
            "DROP TABLE IF EXISTS Screen;"};

	String []tableNames= {"Movies",
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
                         "FOREIGN KEY (MovieID) REFERENCES Movies(MovieID)," +
                         "FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID)" +
                         ");";

     String createSeatsTable = "CREATE TABLE Seats(" +
                 "SeatID INT AUTO_INCREMENT PRIMARY KEY," +
                 "ScreenID INT NOT NULL," +
                 "SeatRow INT NOT NULL," +
                 "SeatColumn INT NOT NULL," +
                 "FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID)" +
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
                    "FOREIGN KEY (CustomerID) REFERENCES CustomersInfo(CustomerID)" +
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
                   "FOREIGN KEY (BookingNum) REFERENCES BookInfo(BookingNum)," +
                   "FOREIGN KEY (ScreenID) REFERENCES Screen(ScreenID)," +
                   "FOREIGN KEY (ScreeningID) REFERENCES ScreeningInfo(ScreeningID)," +
                   "FOREIGN KEY (SeatID) REFERENCES Seats(SeatID)" +
                   ");";
    
  
    String insertMovies="INSERT INTO Movies (MovieTitle, RunningTime, FilmRating, Director, Actors, Genre, MovieDescription, ReleaseDate, MovieRating) VALUES"+
    		"('노인을 위한 나라는 없다.', 122, 'R', '코엔 형제', '배토미 리 존스, 하비에르 바르뎀', '스릴러', '코맥 매카시의 동명 소설을 원작으로 한 영화로, 범죄와 폭력을 중심으로 인간의 본성과 도덕적 혼란을 그린 스릴러입니다.', '2024-01-01', 9.0),"+
    		"('조디악', 157, 'R', '데이비드 핀처', '제이크 질렌할, 로버트 다우니 주니어', '미스터리', '미국의 연쇄살인마 조디악 킬러에 관한 데이비드 핀처의 영화로 실화를 충실히 다뤘다.', '2024-01-04', 8.0),"+
    		"('화양연화', 99, 'PG', '양가위', '양조위, 장만옥', '로맨스', '영화의 제목 화양연화(花樣年華)는 인생에서 꽃과 같이 가장 아름답고 행복한 시간을 의미한다.', '2024-01-03', 9.5),"+
    		"('월-E', 98, 'G', '앤드루 스탠튼', '벤 버트, 엘리사 나이트', '애니메이션', '쓰레기 더미에 파묻힌 지구에 홀로 남겨진 청소 로봇 월-E와 식물을 탐색하기 위해 지구에 온 탐사 로봇 이브와의 사랑을 다룬다.', '2024-01-07', 8.5),"+
    		"('올드보이', 120, 'R', '박찬욱', '최민식, 유지태', '복수', '동명의 만화를 원안으로 한 박찬욱 감독, 최민식, 유지태, 강혜정 주연의 스릴러 느와르 영화.', '2024-01-09', 9.0),"+
    		"('보헤미안 랩소디', 134, 'PG-13', '브라이언 싱어', '라미 말렉, 루시 보인턴', '전기', '전설적인 록 그룹 퀸의 보컬리스트 프레디 머큐리의 전기 영화다.', '2024-01-12', 8.0),"+
    		"('그린북', 130, 'PG-13', '피터 패럴리', '비고 모텐슨, 마허샬라 알리', '코미디', '비고 모텐슨과 마허샬라 알리가 주연을 맡은 피터 패럴리 감독의 로드 무비.', '2024-01-15', 7.5),"+
    		"('1917', 119, 'R', '샘 멘데스', '조지 맥케이, 딘-찰스 채프먼', '전쟁', '제1차 세계대전 중 소식을 전달하기 위한 두 병사의 여정', '2024-01-19', 8.3),"+
    		"('조커', 122, 'R', '토드 필립스', '호아킨 피닉스', '드라마', '사회로부터 버림받은 한 남자가 조커가 되어가는 과정을 그린 영화', '2024-01-02', 8.5),"+
    		"('어벤져스: 엔드게임', 181, 'PG-13', '안소니 루소, 조 루소', '로버트 다우니 주니어, 크리스 에반스', '액션', '어벤져스 팀이 타노스를 상대로 최후의 전투를 벌이는 이야기', '2024-01-24', 8.4),"+
    		"('덩케르트', 106, 'PG-13', '크리스토퍼 놀란', '톰 하디, 마크 라이런스', '전쟁', '제2차 세계대전 중 덩케르크에서의 대피 작전을 그린 영화', '2024-01-20', 7.9),"+
    		"('컨저링', 112, 'R', '제임스 완', '베라 파미가, 패트릭 윌슨', '공포', '실화를 바탕으로 한 페론 가족의 초자연적 현상을 조사하는 이야기', '2024-01-10', 7.5);";


}
