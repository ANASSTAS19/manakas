package ru.manakas.study.java_bot.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.manakas.study.java_bot.Model.ModelJokes;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotService {

    private final TelegramBot telegramBot;
    private final ServiceJokes jokeService; // Зависимость сервиса для работы с анекдотами

    private boolean isAddingJoke = false;
    private boolean isViewingJoke = false;
    private boolean isDeletingJoke = false;
    private boolean isEditingJoke = false;
    private String jokeIdForEdit;

    public TelegramBotService(@Autowired TelegramBot telegramBot, @Autowired ServiceJokes jokeService) {
        this.telegramBot = telegramBot;
        this.jokeService = jokeService;
        this.telegramBot.setUpdatesListener(updates -> { //Лямбда - регистрируем слушателя обновлений
            updates.forEach(this::handleUpdate); //В лямбде забираем все обновления - и вызываем обработку их
            return UpdatesListener.CONFIRMED_UPDATES_ALL; //Подтверждаем, что все забрали
        }, Throwable::printStackTrace);
    }

    private void buttonClickReact(Update update) { //Реагируем на событие
        //Подготавливаем сообщение на ответ
        SendMessage request = new SendMessage(update.message().chat().id(), "Я не знаю таких команд,давай заново(") //update.message().chat().id() - Id, в какой чат отправлять сообщение, в данном случае - тому, кто написал
                .parseMode(ParseMode.HTML) //Без понятия, что такое, но было в документации
                .disableWebPagePreview(true) //Без понятия, что такое, но было в документации
                .disableNotification(true) //Без понятия, что такое, но было в документации
                .replyToMessageId(update.message().messageId()); //Делаем наш ответ как ответ на отправленное ранее сообщение
        this.telegramBot.execute(request); //Отправляем подготовленное сообщение
    }

    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String command = update.message().text();

            if (command.equals("/start")) {
                // Отправляем приветственное сообщение
                sendMessage(update.message().chat().id());
            } else if (command.equals("/add")) {
                // Устанавливаем флаг добавления шутки
                isAddingJoke = true;
                // Просим пользователя ввести текст шутки
                handleAddJoke(update.message().chat().id());
            } else if (command.equals("/all")) {
                // Выводим список всех шуток
                handleViewAllJoke(update.message().chat().id());
            } else if (command.equals("/edit")) {
                // Устанавливаем флаг редактирования шутки
                isEditingJoke = true;
                // Просим пользователя ввести ID шутки для редактирования
                handleAskEditJoke(update.message().chat().id());
            } else if (command.equals("/delete")) {
                // Устанавливаем флаг удаления шутки
                isDeletingJoke = true;
                // Просим пользователя ввести ID шутки для удаления
                handleAskForDeleteJoke(update.message().chat().id());
            } else if (command.equals("/view")) {
                // Устанавливаем флаг просмотра шутки
                isViewingJoke = true;
                // Просим пользователя ввести ID шутки для просмотра
                handleAskViewJoke(update.message().chat().id());
            } else if (isAddingJoke) {
                // Сбрасываем флаг добавления шутки
                isAddingJoke = false;
                // Обрабатываем текст шутки
                handleJokeText(update.message().chat().id(), command);
            } else if (isViewingJoke) {
                // Сбрасываем флаг просмотра шутки
                isViewingJoke = false;
                // Обрабатываем ID шутки для просмотра
                handleViewJoke(update.message().chat().id(), command);
            } else if (isEditingJoke) {
                // Проверяем, введен ли уже ID шутки
                if (jokeIdForEdit == null) {
                    // Запоминаем ID шутки для редактирования
                    jokeIdForEdit = command;
                    // Просим пользователя ввести новый текст шутки
                    SendMessage request = new SendMessage(update.message().chat().id(), "Введите новый текст шутки:");
                    this.telegramBot.execute(request);
                } else {
                    // Вызываем функцию для изменения текста шутки
                    handleEditJoke(update.message().chat().id(), jokeIdForEdit, command);
                    // Сбрасываем переменную ID для редактирования
                    jokeIdForEdit = null;
                    // Сбрасываем флаг редактирования
                    isEditingJoke = false;
                }
            } else if (isDeletingJoke) {
                // Сбрасываем флаг удаления шутки
                isDeletingJoke = false;
                // Вызываем функцию для удаления шутки
                handleDeleteJoke(update.message().chat().id(), command);
            } else {
                buttonClickReact(update);
            }
        }
    }

    private void handleAddJoke(Long chatId) {
        // Отправляем сообщение с просьбой ввести текст шутки
        SendMessage request = new SendMessage(chatId, "Введите текст шутки для добавления");
        this.telegramBot.execute(request);
    }

    private void handleJokeText(Long chatId, String jokeText) {
        // Создаем объект JokeModel с полученным текстом шутки
        ModelJokes jokeModel = new ModelJokes();
        jokeModel.setText(jokeText);

        // Вызываем метод сервиса для создания новой шутки
        Optional<ModelJokes> response = this.jokeService.createJoke(jokeModel);

        // Проверяем результат создания шутки
        if (response.isPresent()) {
            // Шутка успешно добавлена, отправляем подтверждение
            SendMessage responseMessage = new SendMessage(chatId, "Шутка успешно добавлена!");
            this.telegramBot.execute(responseMessage);
        } else {
            // Не удалось добавить шутку, отправляем сообщение об ошибке
            SendMessage responseMessage = new SendMessage(chatId, "Не удалось добавить шутку");
            this.telegramBot.execute(responseMessage);
        }
    }

    private void handleViewAllJoke(Long chatId) {
        // Получаем список всех шуток из сервиса
        List<ModelJokes> allJokes = jokeService.getAllJokes();

        // Формируем строку с текстом ответа, включающую все шутки
        StringBuilder response = new StringBuilder("Список всех анекдотов:\n");
        for (ModelJokes joke : allJokes) {
            // Добавляем текст шутки
            response.append("Шутка: ").append(joke.getText()).append("\n");
            // Добавляем ID шутки
            response.append("ID: ").append(joke.getId()).append("\n");
            // Добавляем дату создания
            response.append("Дата создания: ").append(joke.getCreatedDate()).append("\n");
            // Добавляем дату обновления
            response.append("Дата обновления: ").append(joke.getUpdatedDate()).append("\n");
            // Добавляем пустую строку для разделения шуток
            response.append("\n");
        }

        // Отправляем сообщение с текстом всех шуток
        SendMessage request = new SendMessage(chatId, response.toString());
        this.telegramBot.execute(request);
    }

    private void sendMessage(Long chatId) {
        // Отправляем приветственное сообщение с описанием функционала
        SendMessage request = new SendMessage(chatId, "Привет! Я анекдотический чат-бот. У меня есть команды:");
        this.telegramBot.execute(request);
    }

    private void handleAskViewJoke(Long chatId) {
        // Отправляем сообщение с просьбой ввести ID шутки
        SendMessage request = new SendMessage(chatId, "Введите ID шутки, которую хотите посмотреть:");
        this.telegramBot.execute(request);
    }

    private void handleViewJoke(Long chatId, String jokeId) {
        // Пытаемся преобразовать введенный текст в числовой ID
        try {
            Long jokeIdLong = Long.valueOf(jokeId);

            // Запрашиваем шутку по ID
            Optional<ModelJokes> joke = jokeService.getJokeById(jokeIdLong);

            // Проверяем, найдена ли шутка по ID
            if (joke.isPresent()) {
                // Отправляем сообщение с текстом шутки
                SendMessage request = new SendMessage(chatId, "Шутка: " + joke.get().getText());
                this.telegramBot.execute(request);
            } else {
                // Шутка не найдена, отправляем сообщение об ошибке
                SendMessage request = new SendMessage(chatId, "Шутка с таким ID не найдена");
                this.telegramBot.execute(request);
            }
        } catch (NumberFormatException e) {
            // Введенный текст не является числом, отправляем сообщение об ошибке
            SendMessage request = new SendMessage(chatId, "Некорректный ID. Введите числовое значение.");
            this.telegramBot.execute(request);
        }
    }

    private void handleAskForDeleteJoke(Long chatId) {
        // Отправляем сообщение с просьбой ввести ID шутки для удаления
        SendMessage request = new SendMessage(chatId, "Введите ID шутки для удаления:");
        this.telegramBot.execute(request);
    }

    private void handleDeleteJoke(Long chatId, String jokeId) {
        // Пытаемся преобразовать введенный текст в числовой ID
        try {
            Long jokeIdLong = Long.valueOf(jokeId);
            Optional<ModelJokes> joke = jokeService.getJokeById(jokeIdLong);
            // Проверяем наличие шутки с введенным ID
            if (joke.isPresent()) {
                // Удаляем шутку из сервиса
                jokeService.deleteJokeById(jokeIdLong);

                // Отправляем сообщение об успешном удалении
                SendMessage request = new SendMessage(chatId, "Шутка успешно удалена!");
                this.telegramBot.execute(request);
            } else {
                // Отправляем сообщение о том, что шутки с таким ID не существует
                SendMessage request = new SendMessage(chatId, "Шутка с таким ID не найдена!");
                this.telegramBot.execute(request);
            }
        } catch (NumberFormatException e) {
            // Введенный текст не является числом, отправляем сообщение об ошибке
            SendMessage request = new SendMessage(chatId, "Некорректный ID шутки. Введите числовой ID.");
            this.telegramBot.execute(request);
        } catch (Exception e) {
            // Произошла ошибка при удалении, отправляем сообщение об ошибке
            SendMessage request = new SendMessage(chatId, "Ошибка при удалении шутки: " + e.getMessage());
            this.telegramBot.execute(request);
        }
    }

    private void handleEditJoke(Long chatId, String jokeId, String newJokeText) {
        // Пытаемся преобразовать введенный текст в числовой ID
        try {
            Long jokeIdLong = Long.parseLong(jokeId);

            // Проверяем, существует ли шутка с указанным ID
            Optional<ModelJokes> existingJoke = jokeService.getJokeById(jokeIdLong);
            if (existingJoke.isPresent()) {
                // Создаем объект JokeModel с обновленным текстом
                ModelJokes updatedJoke = new ModelJokes();
                updatedJoke.setId(jokeIdLong);
                updatedJoke.setText(newJokeText);

                // Вызываем метод сервиса для обновления шутки
                jokeService.changeJokeById(updatedJoke.getId(), updatedJoke);

                // Отправляем сообщение об успешном изменении
                SendMessage request = new SendMessage(chatId, "Шутка успешно изменена!");
                this.telegramBot.execute(request);
            } else {
                // Шутка с указанным ID не найдена, отправляем сообщение об ошибке
                SendMessage request = new SendMessage(chatId, "Шутка с таким ID не найдена");
                this.telegramBot.execute(request);
            }
        } catch (Exception e) {
            // Произошла ошибка при изменении, отправляем сообщение об ошибке
            SendMessage request = new SendMessage(chatId, "Ошибка при изменении шутки: " + e.getMessage());
            this.telegramBot.execute(request);
        }
    }

    private void handleAskEditJoke(Long chatId) {
        // Сбрасываем переменную ID для редактирования
        jokeIdForEdit = null;

        // Отправляем сообщение с просьбой ввести ID шутки для редактирования
        SendMessage request = new SendMessage(chatId, "Введите ID шутки для редактирования:");
        this.telegramBot.execute(request);
    }

}
