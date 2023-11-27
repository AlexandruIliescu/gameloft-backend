# Spring Boot News Scraper Application

## Project Description
This Spring Boot application is designed to scrape news and video feeds every 10 minutes and manage the data persistence in a database. It also ensures the creation of local backups for each feed content on every run and appropriately logs any exceptions that occur during the process.

### Key Functionalities
- **Data Scraping**: Automatically scrapes news feeds at regular intervals.
- **Data Storage**: Aggregates and stores essential information from each feed item such as title, description, thumbnail, link, and publish date in the database.
- **Local Backups**: Creates backups of each feed content locally every time the scraper runs.
- **Data Management**: Adds new feed items to the database and deletes those that are no longer available on the source website.
- **API Delivery**: Provides backend APIs that deliver the content of the current news and video tables according to the frontend’s requirements.

### Feed Item Details
Each feed item stored in the database will contain the following information:
- Title
- Description
- Thumbnail
- Link
- Publish Date

### Scheduled Operations
The scraper performs the following operations on each run:
- Adds newly published feed items to the database.
- Deletes articles that are no longer present on the Eurogamer website.

## Installation and Setup

Before running the application, make sure you have Java JDK 17 and Maven installed on your machine. You will also need access to a database where the application will store the scraped data.

1. **Clone the repository**:
   ```bash
   git clone https://github.com/AlexandruIliescu/gameloft-backend
   ```

2. **Navigate to the project directory**:
   ```bash
   cd your-repo
   ```

3. **Configure the database**:
   Update `src/main/resources/application.properties` with your database credentials and connection details.

4. **Build the application**:
   ```bash
   mvn clean install
   ```

5. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   Alternatively, you can run the packaged JAR file:
   ```bash
   java -jar target/news-scraper-0.0.1-SNAPSHOT.jar
   ```

## API Endpoints
The application provides the following endpoints:
- `GET /api/news`: Retrieve a paginated list of news items.
- `GET /api/news/{id}`: Get details of a specific news item.
- `GET /api/news/search`: Search for news items based on a query.