# Android-Native-News

This repository houses an News App that I developed while following the [tutorial series](https://www.youtube.com/playlist?list=PLQkwcJG4YTCRF8XiCRESq1IFFW8COlxYJ) by [Philipp Lackner](https://github.com/philipplackner). The tutorial covered essential topics such as MVVM architecture, Retrofit, Coroutines, Navigation Components, and importantly, integrating a Room database to save and manage favorite articles. The app utilizes the [NewsAPI](https://newsapi.org/) for fetching news articles.

## Screenshots

![Breaking News](https://github.com/duchoan6814/Android-Native-News/blob/master/demo/Screenshot_1691600855.png) ![Article Detail](https://github.com/duchoan6814/Android-Native-News/blob/master/demo/Screenshot_1691600874.png)


## Tutorial Source

The [tutorial series](https://www.youtube.com/playlist?list=PLQkwcJG4YTCRF8XiCRESq1IFFW8COlxYJ) by [Philipp Lackner](https://github.com/philipplackner) can be found on his [YouTube channel](https://www.youtube.com/@PhilippLackner). The series covers various topics related to modern Android app development, including:

- MVVM architecture
- Consuming RESTful APIs using Retrofit
- Local data storage using Room
- Asynchronous programming with Coroutines
- In-app navigation using Navigation Components

The tutorial series was incredibly helpful in understanding these important concepts and how they come together to create a robust and well-structured Android application.

## Features

The features of this News App are based on the lessons learned from the tutorial series, and they include:

- Fetching and displaying news articles from various sources.
- Implementing a responsive UI using Material Components for a modern look and feel.
- Efficiently managing asynchronous operations using Coroutines.
- Saving articles as favorites using Room database integration.
- Utilizing the MVVM architecture for clean code separation and maintainability.
- Navigating between different app screens using Navigation Components.

## Getting Started

To run this project on your local machine, follow these steps:

- Clone the repository: `git clone https://github.com/duchoan6814/Android-Native-News.git`
- Open the project in Android Studio.
- Build and run the app on an emulator or physical device.

Make sure to refer to the tutorial series for guidance on obtaining and setting up your [NewsAPI](https://newsapi.org/) key to enable fetching of news articles.

## API Key Setup

This app requires an API key to fetch news articles. Follow these steps to set up your API key:

- Visit [NewsAPI](https://newsapi.org/) and sign up for an account.
- Once registered, create a new API key.
- Open the `gradle.properties` file and add your API key: `API_KEY="your_api_key"`

## Contribution

Contributions to this project are encouraged and welcome! If you have ideas for improvements, new features, or bug fixes, please submit a pull request.

---

Big thanks to [Philipp Lackner](https://github.com/philipplackner) for his insightful tutorial series. If you have any questions or suggestions, please don't hesitate to reach out.

***Maintainer:*** Truong Duc Hoan<br>
***Email:*** hoantruong6814@gmail.com
