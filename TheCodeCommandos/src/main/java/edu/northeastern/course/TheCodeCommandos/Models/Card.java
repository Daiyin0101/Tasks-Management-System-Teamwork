package edu.northeastern.course.TheCodeCommandos.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

public class Card implements Comparable<Card> {
    // Data field for Board
    private final String cardName;
    private final String status;
    private final ObjectProperty<LocalDate> dueDate;
    private String board;

    // Constructor
    public Card(String cardName, String status, LocalDate dueDate, String board) {
        this.cardName = cardName;
        this.status = status;
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.board = board;
    }

    // Four getter methods
    public String getCardName() {return cardName;}

    public String getStatus() {return status;}

    public ObjectProperty<LocalDate> dueDateProperty() {return dueDate;}

    public String getBoardForCard() {return board;}

    // Set board for card
    public void setBoardForCard(String boardTitle) {
        this.board = boardTitle;
    }
    
    // Add a card
    public void add() {
        Model.getInstance().getDatabaseDriver().addCard(cardName, status, dueDate.get(), board);
    }

    // Delete a card
    public void delete() {
        Model.getInstance().getDatabaseDriver().deleteCard(cardName, board);
    }

    @Override
    public int compareTo(Card card) {
        return this.dueDate.get().compareTo(card.dueDateProperty().get());
    }

}