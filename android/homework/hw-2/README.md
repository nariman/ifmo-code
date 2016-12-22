# Домашнее задание №2

## Цель

Написать приложение, состоящее из одного экрана со списком популярных фильмов, используя The Movie DB API [https://www.themoviedb.org/](https://www.themoviedb.org/). Документация по API доступна здесь: [https://developers.themoviedb.org/3/getting-started](https://developers.themoviedb.org/3/getting-started). Принцип работы приложения повторяет рассмотренное на практике приложение со списков веб-камер ([https://github.com/IFMO-Android-2016/practice2](https://github.com/IFMO-Android-2016/practice2)). Основные идеи, которые надо реализовать в этом приложении в целом повторяют идеи из практики:

* Загрузка данных в `AsyncTaskLoader`
* Отображение данных в `RecycleView`
* Выполнение запроса при помощи `HttpURLConnection`
* Разбор ответа от API при помощи `JsonReader` или `JSONObject`
* Отображение процесса загрузки при помощи `ProgressBar`

<img src="https://github.com/IFMO-Android-2016/homework2/blob/master/demo_screenshot.png" width="360px"/>

## Задание

* Зарегистрироваться на https://www.themoviedb.org/ и получить ключ API, прописать ключ в классе `TmdbApi` вместо дефолтного.
* В методе `TmdbApi.getPopularMoviesRequest` написать код, который создает запрос популярных фильмов, как описано в документации: https://developers.themoviedb.org/3/movies/get-popular-movies (для начала можно запрашивать только первую страницу результата). В качестве параметра запроса должен передаваться язык пользователя (системный), который можно узнать при помощи вызова `Locale.getDefault().getLanguage()`.
* Написать код парсера, который при помощи `JSONObject` или `JsonReader` разбирает ответ от API и возвращает результат типа `List<Movie>` (класс `Movie` уже есть в коде задания).
* Написать класс, наследующий от `AsyncTaskLoader<LoadResult<List<Movie>>>`, который в фоне выполняет запрос к API и разбирает результат. Результатом загрузки должен быть объект типа `LoadResult<List<Movie>>`. (Классы `LoadResult` и `Movie` уже есть в коде задания).
* Написать код и верстку основного экрана в классе `PopularMoviesActivity`, который:
  * При старте показывает индикатор процесса загрузки
  * После завершения загрузки показывает список фильмов в `RecyclerView` (нужно будет написать адаптер)
  * В случае ошибки показывает сообщение об ошибке
  * В случае отсутствия соединения показывает сообщение об отсутствии соединения

## Может пригодиться

Классы из Android SDK, которые надо использовать в задании:

* https://developer.android.com/reference/android/support/v4/content/AsyncTaskLoader.html (документацию можно посмотреть здесь: https://developer.android.com/reference/android/content/AsyncTaskLoader.html)
* https://developer.android.com/reference/android/support/v4/app/LoaderManager.html (документацию можно посмотреть здесь: https://developer.android.com/reference/android/app/LoaderManager.html)
* https://developer.android.com/reference/java/net/HttpURLConnection.html
* https://developer.android.com/reference/android/util/JsonReader.html
* https://developer.android.com/reference/org/json/JSONObject.html
* https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
* https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html
* https://developer.android.com/reference/java/util/Locale.html

Для отображения изображений постеров можно использовать библиотеку Fresco (http://frescolib.org/index.html), зависимость на которую уже добавлена в код задания. Из этой библиотеки понадобится один класс `SimpleDraweeView`, описание здесь: [http://frescolib.org/docs/getting-started.html](http://frescolib.org/docs/getting-started.html). 

## Требования

* При повороте экрана НЕ должна происходить повторная загрузка данных
* Долгие операции НЕ должны выполняться в UI потоке
* Во время загрузки должен показывать индикатор процесса загрузки
* В случае ошибок или отсутсвия соединения должны показываться адекватные сообщения об ошибках
* Приложение должно нормально выдерживать поворот экрана (с сохранением контента и без лишних действий)
* Элемент списка фильмов должен содержать как минимум: изображение постера с правильными пропорциями, название и описание фильма на языке пользователя.
* При необходимости верстка должна быть адаптирована к ландшафтной ориентации экрана (например, если постер в портретной ориентации может занимать всю ширину экрана, то в ландшафтной ориентации это недопустимо)

## Оценка

* 2 балла - за правильное выполнение запроса и обработку искулючений в загрузчике
* 2 балла - за разбор ответа на запрос в формате JSON
* 4 балла - за отображение списка фильмов
* 2 балла - за отображение ошибок и отсуствия соединения
* +3 балла сверх нормы - если при смене языка в системных настройках будет делаться новый запрос с другим языком и отображаться обнвленный контент с сохранением положения списка
* +3 балла сверх нормы - если будет реализована подгрузка следующих страниц результата по мере прокрутки списка вниз

Сдавать в виде пул-реквеста, в комментарии указать имя. Если выполнены задания сверх нормы -- написать об этом в комментарии (иначе смотреть не будем).
