package edu.northeastern.course.TheCodeCommandos.Models;

import edu.northeastern.course.TheCodeCommandos.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashSet;

public class Model {

	private static Model model;
	private final ViewFactory viewFactory;
	private final DatabaseDriver databaseDriver;

	private Member member;
	private boolean memberLoginSuccessFlag;
	private final ObservableList<Board> boards;
	private final ObservableList<Card> boardCards;
	private final ObservableList<Card> allCards;
	private final ObservableList<Member> allMembers;

	private Board currentBoard;

	// Create everything
	private Model() {
		this.viewFactory = new ViewFactory();
		this.databaseDriver = new DatabaseDriver();

		this.memberLoginSuccessFlag = false;
		this.member = new Member("", "", "",  null);
		this.boards = FXCollections.observableArrayList();
		this.boardCards = FXCollections.observableArrayList();
		this.allCards = FXCollections.observableArrayList();
		this.allMembers = FXCollections.observableArrayList();
		this.currentBoard = new Board("", "", null);
	}

	// Static method: create a model instance
	public static synchronized Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	// Get view factory through model
	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	// Get database through model
	public DatabaseDriver getDatabaseDriver() {return databaseDriver;}

	// If user has successfully logged in, return true, otherwise return false
	public boolean getMemberLoginSuccessFlag() {
		return memberLoginSuccessFlag;
	}

	// Set whether the user has successfully logged in
	public void setMemberLoginSuccessFlag(boolean flag) {
		this.memberLoginSuccessFlag = flag;
	}

	// Get the member who logged in
	public Member getMember() {
		return member;
	}

	// Set allMembers data field through database ResultSet
	public void setAllMembers() {
		ResultSet resultSet = databaseDriver.getAllMemberData();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");
				String username = resultSet.getString("Username");
				String[] dateParts = resultSet.getString("Date").split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
				allMembers.add(new Member(firstName, lastName, username, date));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Get allMembers data field
	public ObservableList<Member> getAllMembers() {
		return  allMembers;
	}

	// Check the username and password through database ResultSet
	public void evaluateClientCredentials(String username, String password) {
		ResultSet resultSet = databaseDriver.getMemberData(username, password);
		try {
			if (resultSet.isBeforeFirst()) {
				String fName = resultSet.getString("FirstName");
				String lName = resultSet.getString("LastName");
				String userName = resultSet.getString("Username");
				String[] dateParts = resultSet.getString("Date").split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.member = new Member(fName, lName, userName, date);
				this.memberLoginSuccessFlag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Set boards data field through database ResultSet
	public void setBoards() {
		ResultSet resultSet = databaseDriver.getBoardsData();
		try {
			while (resultSet.next()){
				String boardTitle = resultSet.getString("BoardTitle");
				String description = resultSet.getString("Description");
				String[] dateParts = resultSet.getString("Date").split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
				boards.add(new Board(boardTitle, description, date));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Get boards data field
	public ObservableList<Board> getBoards() {
		return boards;
	}

	public void setCardsForBoard(String boardTitle) {
		ResultSet resultSet = databaseDriver.getCardsForBoard(boardTitle);
		try {
			while (resultSet.next()){
				String cardName = resultSet.getString("CardName");
				String status = resultSet.getString("Status");
				String[] dateParts = resultSet.getString("Date").split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
				boardCards.add(new Card(cardName, status, date, boardTitle));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Get cards for the specified board
	public ObservableList<Card> getBoardCards() {
		return boardCards;
	}

	// Get current board
	public Board getCurrentBoard() {
		return currentBoard;
	}

	// Set current board
	public void setCurrentBoard(Board currentBoard) {
		this.currentBoard = currentBoard;
	}
	
	// Set all cards
	public void setAllCards() {
		ResultSet resultSet = databaseDriver.getAllCards();
		try {
			while (resultSet.next()){
				String cardName = resultSet.getString("CardName");
				String status = resultSet.getString("Status");
				String[] dateParts = resultSet.getString("Date").split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
				String board = resultSet.getString("Board");
				allCards.add(new Card(cardName, status, date, board));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	// Get all cards
	public ObservableList<Card> getAllCards() {
		return allCards;
	}
	
	// Create HashSet by board title
	public HashSet<String> getBoardTitleHashSet() {
		HashSet<String> boardTitleHashSet = new HashSet<>();
		for (Board board : boards) {
            boardTitleHashSet.add(board.getBoardTitle());
		}
		return boardTitleHashSet;
	}
	
	// Create HashSet by card name
	public HashSet<String> getCardNameHashSet() {
		HashSet<String> cardNameHashSet = new HashSet<>();
		for (Card c: getBoardCards()) {
			cardNameHashSet.add(c.getCardName());
		}
		return cardNameHashSet;
	}
	
	// Create HashSet by username
	public HashSet<String> getMemberUsernameHashSet() {
		HashSet<String> memberUserHashSet = new HashSet<>();
		for (Member m: getAllMembers()) {
			memberUserHashSet.add(m.getUsername());
		}
		return memberUserHashSet;
	}
}
