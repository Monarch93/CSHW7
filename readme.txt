Описание работы приложения Simple.Nas

Simple.Nas — это двухкомпонентное приложение, предоставляющее пользователям хранить файлы в удалённом хранилище. Приложение состоит из серверной и клиентской частей. Серверная часть предназначена для хранения файлов пользователей. Клиентская часть предназначена для предоставления пользователю графического интерфейса для работы с файлами на сервере.

Операции, которые доступны пользователю при работе с файлами:
•	загрузка файла с локального ПК на сервер,
•	загрузка файла с сервера на локальный ПК,
•	создание локальной или удалённой папки,
•	переименование папки или файла,
•	удаление файла или папки. 

Авторизация и удалённое дисковое пространство

Серверная часть приложения (далее сервер) при запуске ожидает подключения клиентов. При подключении клиента сервер выделяет ему канал связи. Для доступа к своему дисковому пространству на сервере, клиент должен авторизоваться. Авторизация состоит в сообщении серверу уникального логина, состоящего из букв и цифр. Получив от клиента логин, сервер предоставляет клиенту доступ к файлам, расположенным в папке, название которой совпадает с логином. Если такая папка не была найдена, то сервер создаст её и предоставит клиенту доступ к её содержимому.

Клиент не имеет возможности выйти за пределы своего удалённого дискового пространства, но имеет полный контроль над его содержимым.

Графический интерфейс клиента

Графический интерфейс клиента (далее GUI) реализован в виде небольшого приложения с графическим интерфейсом. Окно приложения разделено на две панели: левая отображает выбранную пользователем локальную папку, а правая — выбранную удалённую папку, папку на стороне сервера. При запуске GUI подключение к серверу отсутствует, и правая панель пуста.

Авторизация клиента

Над панелями расположены два поля ввода, предназначенные для ввода и отображения путей к локальной и удалённой папкам. Поле ввода пути для локальной папки, как и левая панель доступны для использования сразу после запуска приложения. Поле ввода пути к удалённой папке на этом этапе служит для ввода логина; введя в него логин и нажав ENTER пользователь запускает процедуру авторизации на сервере.

Если авторизация прошла успешно, поле ввода пути удалённой папки содержит путь к текущей удалённой папке, а правая панель отображает содержимое текущей удалённой папки. Поле ввода пути удалённой папки становится доступно для указания пути вручную.

Если авторизация отклонена сервером, то соединение с сервером обрывается. Возможные причины отклонения авторизации:
•	пользователь использовал недопустимые символы при вводе логина. Допустимыми символами являются буквы и цифры;
•	пользователь с указанным логином уже авторизован.

Навигация

Перемещение между папками — локальными и удалёнными — пользователь может осуществлять двумя способами: редактируя строку пути в соответствующем поле ввода, или при помощи мыши.

Для указания пути к интересующей папке в поле ввода, пользователь должен в соответствующем поле ввода отредактировать строку пути к интересующей папке и нажать ENTER.

При помощи мыши навигацию производить легче: если требуется перейти в родительскую папку, то нужно нажать кнопку с треугольником, расположенную справа от соответствующего поля ввода. Если наоборот требуется зайти в одну из папок, отображаемых в панели, то нужно сделать двойной щелчок по этой папке.

Создание папок, удаление и переименование файлов и папок

Пользователь GUI-приложения имеет возможность создавать папки как на стороне сервера, так и на своей стороне. Для создания папки нужно правой кнопкой мыши вызвать контекстное меню и выбрать в нём соответствующий пункт.

Для переименования папки или файла нужно выделить интересующий элемент текущего каталога, правой кнопкой мыши вызвать контекстное меню и выбрать в нём соответствующий пункт. Переименование выбранного пункта текущего каталога можно начать при помощи клавиши F2. Переименование следует завершать нажатием ENTER. Для отказа от начатого переименования следует нажать ESC.

Для удаления папки или файла пользователь должен выделить соответствующий элемент текущего каталога, правой кнопкой мыши вызвать контекстное меню и выбрать пункт «Удалить». При удалении файлов и непустых папок пользователю будет предоставлена возможность подтвердить свои намерения.

Внимание!
Удаление файла или папки является необратимым процессом! Также нельзя отменить завершённое переименование файла или папки.

Перемещение файлов

Для перемещения файла на сервер нужно выделить интересующий файл текущего локального каталога, правой кнопкой мыши вызвать контекстное меню и выбрать соответствующий пункт. Отправку файла на сервер можно также запустить кнопкой «Отправить на сервер» расположенной под левой панелью.

Аналогично производится загрузка файла с сервера, но выбирать файл нужно в правой панели. Кнопка для загрузки файла с сервера расположена под правой панелью и называется «Загрузить с сервера».

Перемещение папок или групп файлов не реализовано.

Внимание!
Избегайте пересылки слишком больших файлов. При пересылке файла размером более 3.1 Гб файл может переместиться не полностью. При таком перемещении проверяйте размеры исходного и конечного файлов. Если перемещение большого файла совершенно необходимо, попробуйте повторить попытку или предварительно переместить файл заведомо большего размера. В некоторых случаях эти действия помогут добиться желаемого.

Управление сервером

Сервер не имеет графического интерфейса, и для управления сервером предусмотрены только консольный ввод и только одна команда — exit, которая служит для завершения серверной части приложения. При выполнении этой команды сервер принудительно разрывает все существующие подключения.

