# 📊 Post Performance Analysis

A smart analytics system for tracking and analyzing post engagement performance across different time slots, days, and content patterns. Built with scalability and real-world social media insights in mind.

---

## 🚀 Overview

**Post Performance Analysis** helps you understand *when* and *why* your posts perform better.

It aggregates engagement data (reactions, comments, etc.) and transforms it into actionable insights like:

* Best posting time (day + hour)
* Engagement distribution by weekday
* Top-performing posts
* Time-slot based performance trends
* Smart performance insights dashboard

No guesswork. Just data-driven decisions.

---

## ✨ Features

* 📈 Engagement analytics by day & hour
* 🧠 Smart “Best Posting Time” detection
* 🏆 Top N posts by engagement score
* 📅 Weekly performance breakdown
* ⚡ Fast aggregation using Java Streams
* 📊 Clean dashboard-ready DTO structure
* 🔐 Backend-first architecture (API-driven)

---

## 🧰 Tech Stack

**Backend**

* Java 21+
* Spring Boot
* Spring Web
* Thymeleaf
* Lombok

**Data Processing**

* Java Streams API
* Custom aggregation logic

**Frontend**

* Thymeleaf
* HTML / CSS

---

## 🏗️ Architecture

The system follows a layered architecture:

```
Controller → Service → Meta Graph API Call
```

### Core Modules:

* `AnalysisController` – returns view and sending data to it
* `MetaGraphApiService` – fetches posts for business logic & analytics service
* `AnalyticsService` – aggregation & insights generation

---

## 📊 Key Insight Example

**Best Posting Time:**

```
Monday 16:00 - 17:00
```

This is calculated by grouping posts into time slots and summing engagement scores.

---

## ⚙️ Getting Started

### Option 1: Run with Docker

Pull and run the latest image from Docker Hub:

```bash
docker pull elgunzamanov/post-performance-analysis:v1

docker run -d \
  --name post-performance-analysis \
  -p 8080:8080 \
  -e META_GRAPH_PAGE_ID=<your_meta_graph_page_id> \
  -e META_GRAPH_PAGE_ACCESS_TOKEN=<your_meta_graph_page_access_token> \
  -e META_GRAPH_LIMIT=20
  -e META_GRAPH_PAGE_SIZE=5
  -e META_GRAPH_FIELDS="message,created_time,reactions.summary(true),comments.summary(true)"
  elgunzamanov/post-performance-analysis:v1
```

The application will be available at:

```text
http://localhost:8080
```

### Option 2: Run from Source

#### 1. Clone the repository

```bash
git clone https://github.com/elgunzamanov/post-performance-analysis.git
cd post-performance-analysis
```

#### 2. Configure environment variables

```bash
META_GRAPH_PAGE_ID=<your_meta_graph_page_id>
META_GRAPH_PAGE_ACCESS_TOKEN=<your_meta_graph_page_access_token>
META_GRAPH_LIMIT=20
META_GRAPH_PAGE_SIZE=5
META_GRAPH_FIELDS="message,created_time,reactions.summary(true),comments.summary(true)"
```

#### 3. Run the application

```bash
./gradlew bootRun
```

The application will be available at:

```text
http://localhost:8080
```

---

## 📦 Example Response

```json
{
  "bestDay": "MONDAY",
  "bestHour": 16,
  "engagementScore": 12450
}
```

---

## 🧪 Testing

Run tests with:

```bash
./mvnw test
```

Includes:

* Unit tests for service layer
* Aggregation logic validation

---

## 📌 Future Improvements

* 🔄 Real-time analytics streaming
* 🤖 AI-based posting suggestions
* 📊 Advanced visualization dashboard
* 📥 Export reports (CSV / PDF)
* 🔔 Notification system for optimal posting time

---

## 🤝 Contributing

Pull requests are welcome. For major changes, open an issue first to discuss what you’d like to change.

---

## 🧠 Final Note

This project focuses on turning raw engagement data into **clear, actionable business insights**. The goal is simple: post smarter, not harder.
