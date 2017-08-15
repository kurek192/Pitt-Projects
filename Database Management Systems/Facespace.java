import java.sql.*;
import java.text.ParseException;
import java.util.Scanner;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Facespace {

    private static Connection connection;
    private String query;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public static void main(String[] args) throws SQLException {
		Scanner input;
        String username, password;

        input = new Scanner(System.in);
		System.out.print("Enter username(pitt ID): ");
        username = input.nextLine();
		System.out.print("Enter password(peoplesoft ID): ");
        password = input.nextLine();

        try {
            //register driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

            //set database location
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

            System.out.println("Connecting...");
            //create connection
            connection = DriverManager.getConnection(url, username, password);            
            System.out.println("Connection Successful!");

            mainMenu();

        } catch (Exception e) {
            System.out.println("Error connecting to database! " + e.toString());
            System.exit(0);
        }

        try {
            System.out.println("Disconnecting...");
            connection.close();
        } catch (Exception e) {
            System.out.println("Error disconnecting from database!  " + e.toString());
        }

    }

    public static void mainMenu() {

        Scanner input = new Scanner(System.in);

        int quit = 0;
        while (quit == 0) {
            System.out.print("\n");
            System.out.println("1. createUser");
            System.out.println("2. initiateFriendship");
            System.out.println("3. establishFriendship");
            System.out.println("4. displayFriends");
            System.out.println("5. createGroup");
            System.out.println("6. addToGroup");
            System.out.println("7. sendMessageToUser");
            System.out.println("8. sendMessageToGroup");
            System.out.println("9. displayMessages");
            System.out.println("10. displayNewMessages");
            System.out.println("11. searchForUser");
            System.out.println("12. threeDegrees");
            System.out.println("13. topMessagers");
            System.out.println("14. dropUser");
            System.out.println("15. runAutomatedTests");
            System.out.println("0. QUIT");
            System.out.println("\nWhat is your choice?: ");

            int choice = input.nextInt();
            Facespace fs = new Facespace();

            while (choice < 0 || choice > 15) {
                System.out.println("Please enter a valid choice!");
                choice = input.nextInt();
            }

            input.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter the users full name (FIRST MIDDLE LAST): ");
                    String user = input.nextLine();
                    System.out.println("Enter the users email: ");
                    String email = input.nextLine();
                    System.out.println("Enter the users date of birth (YYYY-MM-DD): ");
                    String dob = input.nextLine();
                    fs.createUser(user, email, dob);
                    break;
                case 2:
                    System.out.println("Enter the first users userID: ");
                    int user1 = input.nextInt();
                    System.out.println("Enter the second users userID: ");
                    int user2 = input.nextInt();
                    fs.initiateFriendship(user1, user2);
                    break;
                case 3:
                    System.out.println("Enter the first friends userID: ");
                    int friend1 = input.nextInt();
                    System.out.println("Enter the second friends userID: ");
                    int friend2 = input.nextInt();
                    fs.establishFriendship(friend1, friend2);
                    break;
                case 4:
                    System.out.println("Enter the user name (FIRST MIDDLE LAST): ");
                    String userfriend = input.nextLine();
                    fs.displayFriends(userfriend);
                    break;
                case 5:
                    System.out.println("Enter the group name: ");
                    String group = input.nextLine();
                    System.out.println("Enter the group description: ");
                    String description = input.nextLine();
                    System.out.println("Enter the group membership limit: ");
                    int limit = Integer.parseInt(input.nextLine());
                    fs.createGroup(group, description, limit);
                    break;
                case 6:
                    System.out.println("Enter the group name: ");
                    group = input.nextLine();
                    System.out.println("Enter the userID to be added to the group: ");
                    user1 = input.nextInt();
                    fs.addToGroup(group, user1);
                    break;
                case 7:
                    System.out.println("Enter message subject: ");
                    String subject = input.nextLine();
                    System.out.println("Enter message body: ");
                    String body = input.nextLine();
                    System.out.println("Enter recipient userID: ");
                    int recipient = input.nextInt();
                    System.out.println("Enter sender userID: ");
                    int sender = input.nextInt();
                    fs.sendMessageToUser(subject, body, recipient, sender);
                    break;
                case 8:
                    System.out.println("Enter message subject: ");
                    subject = input.nextLine();
                    System.out.println("Enter message body: ");
                    body = input.nextLine();
                    System.out.println("Enter groupID: ");
                    int gid = input.nextInt();
                    System.out.println("Enter sender userID: ");
                    sender = input.nextInt();
                    fs.sendMessageToGroup(subject, body, gid, sender);
                    break;
                case 9:
                    System.out.println("Enter userID: ");
                    int uid = input.nextInt();
                    fs.displayMessages(uid);
                    break;
                case 10:
					System.out.println("Enter userID: ");
                    uid = input.nextInt();
                    fs.displayNewMessages(uid);
                    break;
                case 11:
                    System.out.println("Enter string to search for user: ");
                    String searchKey = input.nextLine();
                    fs.searchForUser(searchKey);
                    break;
                case 12:
					System.out.println("Enter userID of UserA: ");
					int usera = input.nextInt();
					System.out.println("Enter userID of UserB: ");
					int userb = input.nextInt();
                    fs.threeDegrees(usera, userb);
                    break;
                case 13:
                    System.out.println("Find k users who have sent the most messages in the last x months");
                    System.out.println("Enter k: ");
                    int k = input.nextInt();
                    System.out.println("Enter x: ");
                    int x = input.nextInt();
                    fs.topMessagers(k, x);
                    break;
                case 14:
                    System.out.println("Enter userID: ");
                    uid = input.nextInt();
                    fs.dropUser(uid);
                    break;
                case 15:
                    System.out.println("Running automated tests...");
                    fs.automatic();
                    break;
                case 0:
                    quit = 1;
                    break;
                default:
                    break;
            }

        }
    }

    public int createUser(String name, String email, String dob) {
        int retId = -1;

        try {
            String[] names = name.split(" ");

            statement = connection.createStatement();
            query = "SELECT MAX(userID) FROM Users";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            int maxUsers = resultSet.getInt(1);

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date bday = new java.sql.Date(df.parse(dob).getTime());
            Date now = new Date();
            String date = df.format(now);
            java.sql.Date login = new java.sql.Date(df.parse(date).getTime());

            String insert = "INSERT INTO Users(userID, fname, mname, lname, email, DOB, loggedIn) VALUES(?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setInt(1, (maxUsers + 1));
            preparedStatement.setString(2, names[0]);
            preparedStatement.setString(3, names[1]);
            preparedStatement.setString(4, names[2]);
            preparedStatement.setString(5, email);
            preparedStatement.setDate(6, bday);
            preparedStatement.setDate(7, login);
            preparedStatement.executeUpdate();
            System.out.println("User successfully created!");
            retId = maxUsers + 1;
        } catch (Exception e) {
            System.out.println("Error adding user to database: "
                    + e.toString());
        } finally {
            try {
                statement.close();
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }

        return retId;
    }

    public void initiateFriendship(int user1, int user2) {

        try {
            String insertQuery = "INSERT INTO Friendships(friend1, friend2, status, established ) VALUES(?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, user1);
            preparedStatement.setInt(2, user2);
            preparedStatement.setInt(3, 0);
            preparedStatement.setNull(4, java.sql.Types.DATE);
            preparedStatement.executeUpdate();
            System.out.println("Pending friendship successfully created!");

        } catch (Exception e) {
            System.out.println("Error adding group to database: "
                    + e.toString());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }

    }

    public void establishFriendship(int friend1, int friend2) {

        try {
            String update = "UPDATE Friendships SET status = 1, established = systimestamp WHERE ((friend1 = " + friend1 + " AND friend2 = " + friend2 + ") OR (friend2 = " + friend1 + " AND friend1 = " + friend2 + "))";
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.executeUpdate();
            System.out.println("Bilateral friendship created!");

        } catch (Exception e) {
            System.out.println("Error adding group to database: "
                    + e.toString());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }

    }

    public void displayFriends(String user) {
        try {
            String[] names = user.split(" ");
            statement = connection.createStatement();
            String select = "SELECT userID FROM Users WHERE fname = '" + names[0] + "' AND mname = '" + names[1] + "' AND lname = '" + names[2] + "'";
            resultSet = statement.executeQuery(select);
            if (!resultSet.next()) {
                System.out.println("User does not exist!");
            } else {

                int userID = resultSet.getInt(1);

                System.out.print("\n");
                System.out.println("PENDING FRIENDSHIPS:");
                System.out.print("---------------------------\n");

                String column;
                select = "SELECT fname, mname, lname FROM Users WHERE userID IN (SELECT friend1 FROM Friendships WHERE friend2 = " + userID + " AND status = 0)";
                resultSet = statement.executeQuery(select);

                while (resultSet.next()) {
                    column = resultSet.getString("fname");
                    System.out.print(column + " ");
                    column = resultSet.getString("mname");
                    System.out.print(column + " ");
                    column = resultSet.getString("lname");
                    System.out.print(column);
                }

                select = "SELECT fname, mname, lname FROM Users WHERE userID IN (SELECT friend2 FROM Friendships WHERE friend1 = " + userID + "AND status = 0)";
                resultSet = statement.executeQuery(select);

                while (resultSet.next()) {
                    column = resultSet.getString("fname");
                    System.out.print(column + " ");
                    column = resultSet.getString("mname");
                    System.out.print(column + " ");
                    column = resultSet.getString("lname");
                    System.out.print(column);
                    System.out.print("\n");
                }
                
                System.out.print("\n");

                System.out.println("ESTABLISHED FRIENDSHIPS:");
                System.out.print("---------------------------\n");

                select = "SELECT fname, mname, lname FROM Users WHERE userID IN (SELECT friend1 FROM Friendships WHERE friend2 = " + userID + " AND status = 1)";
                resultSet = statement.executeQuery(select);

                while (resultSet.next()) {
                    column = resultSet.getString("fname");
                    System.out.print(column + " ");
                    column = resultSet.getString("mname");
                    System.out.print(column + " ");
                    column = resultSet.getString("lname");
                    System.out.print(column);
                }

                select = "SELECT fname, mname, lname FROM Users WHERE userID IN (SELECT friend2 FROM Friendships WHERE friend1 = " + userID + "AND status = 1)";
                resultSet = statement.executeQuery(select);

                while (resultSet.next()) {
                    column = resultSet.getString("fname");
                    System.out.print(column + " ");
                    column = resultSet.getString("mname");
                    System.out.print(column + " ");
                    column = resultSet.getString("lname");
                    System.out.print(column);
                    System.out.print("\n");
                }

            }

        } catch (Exception e) {
            System.out.println("Error displaying friends: "
                    + e.toString());
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }

    }

    public int createGroup(String name, String description, int limit) {
        int retId = -1;
        try {
            if (limit <= 0) {
                System.out.println("Member limit must be greater than 0!");
            } else {
                statement = connection.createStatement();
                query = "SELECT MAX(groupID) FROM Groups";
                resultSet = statement.executeQuery(query);
                resultSet.next();
                int maxGroups = resultSet.getInt(1);

                String insert = "INSERT INTO Groups(groupID, name, memLimit, memCount, description) VALUES(?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(insert);

                preparedStatement.setInt(1, (maxGroups + 1));
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3,limit);
                preparedStatement.setInt(4, 0);
                preparedStatement.setString(5, description);
                preparedStatement.executeUpdate();
                System.out.println("Group successfully created!");
                retId = maxGroups + 1;
            }

        } catch (Exception e) {
            System.out.println("Error adding group to database: "
                    + e.toString());
        } finally {
            try {
                statement.close();
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }

        return retId;

    }

    public void addToGroup(String group, int user) {
        try {
            query = "SELECT COUNT(DISTINCT GROUPID) FROM BELONGS_TO WHERE GROUPID = (SELECT GROUPID FROM GROUPS WHERE NAME = ? )";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, group);
            resultSet = preparedStatement.executeQuery();
            Integer memCount = Integer.MAX_VALUE;
            while(resultSet.next()) {
                memCount = resultSet.getInt(1);
            }

            query = "SELECT MEMLIMIT FROM GROUPS WHERE NAME = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, group);
            resultSet = preparedStatement.executeQuery();
            Integer memMax = 0;
            while(resultSet.next()) {
                memMax = resultSet.getInt(1);
            }

            if(memCount >= memMax) {
                System.out.println("That group is already full so the user was not added");
                return;
            }

            query = "INSERT INTO BELONGS_TO SELECT ?, GROUPID FROM GROUPS WHERE NAME = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user);
            preparedStatement.setString(2, group);
            int rows = preparedStatement.executeUpdate();
            connection.commit();
            System.out.println(rows + " rows were added to the database");

        } catch(Exception e) {
            System.out.println("Error adding to group: " + e.toString());
        } finally {
            closeResources();
        }
    }

    public void sendMessageToUser(String subj, String body, int recipient, int sender) {
        try {
			
            //For generating new messageID
			int totalMessages;
			statement = connection.createStatement();
            query = "SELECT MAX(msgID) FROM Messages";
            resultSet = statement.executeQuery(query);
            resultSet.next();
			totalMessages = resultSet.getInt(1);
			statement.close();
			
            //For generating date
            java.sql.Date dateSent = new java.sql.Date(new java.util.Date().getTime());

			//Insert message
            String insert = "INSERT INTO Messages(msgID, sender, subject, content, dateSent) VALUES(?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setInt(1, (totalMessages + 1));
            preparedStatement.setInt(2, sender);
            preparedStatement.setString(3, subj);
            preparedStatement.setString(4, body);
            preparedStatement.setDate(5, dateSent);
            preparedStatement.executeUpdate();
            preparedStatement.close();
			
			//insert Recipients
            insert = "INSERT INTO Recipients(msgID, recipient) VALUES(?, ?)";
            preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setInt(1, (totalMessages + 1));
            preparedStatement.setInt(2, recipient);
			preparedStatement.executeUpdate();
			
            System.out.println("Message sent successfully!");
            preparedStatement.close();
        } 
        catch (Exception e) {
            System.out.println("Error inserting message into database: "
                    + e.toString());
        } finally {
            try {
                statement.close();
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }
    }

    public void sendMessageToGroup(String subj, String body, int gid, int sender){
        try {
             //For generating new messageID
			int totalMessages;
			statement = connection.createStatement();
            query = "SELECT MAX(msgID) FROM Messages";
            resultSet = statement.executeQuery(query);
            resultSet.next();
			totalMessages = resultSet.getInt(1);
			statement.close();

            //Generate a list of all recipients
            query = "SELECT member FROM Belongs_To WHERE groupID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, gid);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> groupMembers = new ArrayList<Integer>();
            while(resultSet.next()){
                groupMembers.add(resultSet.getInt("member"));
            }
            preparedStatement.close();

            //generate dateSent
            java.sql.Date dateSent = new java.sql.Date(new java.util.Date().getTime());

           //Insert message
            String insert = "INSERT INTO Messages(msgID, sender, subject, content, dateSent) VALUES(?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setInt(1, (totalMessages + 1));
            preparedStatement.setInt(2, sender);
            preparedStatement.setString(3, subj);
            preparedStatement.setString(4, body);
            preparedStatement.setDate(5, dateSent);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            /* Loop through list of group members to make inserts in Recipients for each recipient*/
            for(int i = 0; i < groupMembers.size(); i++){
              
            insert = "INSERT INTO Recipients(msgID, recipient) VALUES(?, ?)";
            preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setInt(1, (totalMessages + 1));
            preparedStatement.setInt(2, groupMembers.get(i));
			preparedStatement.executeUpdate();
            }

            System.out.println("Message to group sent successfully!");
        } 
        catch (Exception e) {
            System.out.println("Error inserting message into database: "
                    + e.toString());
        } finally {
            try {
                statement.close();
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Cannot close statement: " + e.toString());
            }
        }
    }

    public void displayMessages(int user) {
        try {
            //Generate a list of all messages
            query = "SELECT Messages.sender, Messages.subject, Messages.content, Messages.dateSent, Messages.msgID FROM Messages INNER JOIN Recipients ON Messages.msgID = Recipients.msgID WHERE recipient = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user);
            resultSet = preparedStatement.executeQuery();
			
            //Save those messages in a list
            ArrayList<DBMessage> messageList = new ArrayList<DBMessage>();
            int from;
            String s;
            String b;
            Date d;
            int mid;
            DBMessage m;
			
            while(resultSet.next()){
                from = resultSet.getInt("sender");
                s = resultSet.getString("subject");
                b = resultSet.getString("content");
                d = resultSet.getDate("dateSent");
                mid = resultSet.getInt("msgID");
                m = new DBMessage(from, s, b, d, mid);
				System.out.println("Message id = " + m.messageID);
                messageList.add(m);
            }
            preparedStatement.close();
			//print out that list
			System.out.println("\nMessages");
            System.out.println("---------------------------\n");
			for(int i = 0; i < messageList.size(); i++){
				m = messageList.get(i);
				//Get the first, middle, last name of sender
				query = "SELECT * FROM Users WHERE userID = ?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, m.senderID);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				String fullname = resultSet.getString("fname") + " " + resultSet.getString("mname") +  " " + resultSet.getString("lname");
				//print everything out
				System.out.println("From: " + fullname);
				System.out.println("Subject: " + m.subject);
				System.out.println("Sent: " + m.dateSent.toString());
				System.out.println("\n" + m.body);
				System.out.println("---------------------------\n");
				preparedStatement.close();
			}
        } 
        catch (Exception e) {
            System.out.println("Error finding messages: "
                    + e.toString());
        }
    }

    public void displayNewMessages(int user) {
		try {
            //Generate a list of all messages 
            query = "SELECT * FROM Messages INNER JOIN Recipients ON Messages.msgID = Recipients.msgID WHERE recipient = ? AND CAST(dateSent AS TIMESTAMP) > (SELECT loggedIn FROM Users WHERE userID = ?)";
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user);
			preparedStatement.setInt(2, user);
            resultSet = preparedStatement.executeQuery();
			
            //Save those messages in a list
            ArrayList<DBMessage> messageList = new ArrayList<DBMessage>();
            int from;
            String s;
            String b;
            Date d;
            int mid;
            DBMessage m;
            while(resultSet.next()){
                from = resultSet.getInt("sender");
                s = resultSet.getString("subject");
                b = resultSet.getString("content");
                d = resultSet.getDate("dateSent");
                mid = resultSet.getInt("msgID");
                m = new DBMessage(from, s, b, d, mid);
                messageList.add(m);
            }
            preparedStatement.close();
			
			//print out that list
			System.out.println("\nMessages");
            System.out.println("---------------------------\n");
			for(int i = 0; i < messageList.size(); i++){
				m = messageList.get(i);
				//Get the first, middle, last name of sender
				query = "SELECT * FROM Users WHERE userID = ?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, m.senderID);
				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				String fullname = resultSet.getString("fname") + " " + resultSet.getString("mname") +  " " + resultSet.getString("lname");
				//print everything out
				System.out.println("From: " + fullname);
				System.out.println("Subject: " + m.subject);
				System.out.println("Sent: " + m.dateSent.toString());
				System.out.println("\n" + m.body);
				System.out.println("---------------------------\n");
				preparedStatement.close();
			}
        } 
        catch (Exception e) {
            System.out.println("Error finding messages: "
                    + e.toString());
        } 
    }

    /*
     *Takes a space delimited string and searches all possible users with that string in any field
     */
    public void searchForUser(String key) {
        try {
            if(key == null) {
                System.out.println("Your search key was null");
                return;
            }
            query = "SELECT FNAME, MNAME, LNAME FROM USERS WHERE ";
            StringBuilder builder = new StringBuilder();
            builder.append(query);
            String[] keys = key.split("\\s+");
            if(keys.length >= 100) {
                System.out.println("You have entered too many keys at once");
                return;
            }
            for(int i=0; i<keys.length;i++) {
                if(i > 0) {
                    builder.append(" OR ");
                }
                builder.append("FNAME LIKE '%' || ? || '%' OR MNAME LIKE '%' || ? || '%' OR LNAME LIKE '%' || ? || '%' ");
            }
            builder.append(" ORDER BY FNAME, MNAME, LNAME ASC");
            preparedStatement = connection.prepareStatement(builder.toString());
            int ind = 1;
            for(int i=0; i<keys.length; i++) {
                preparedStatement.setString(ind++, keys[i]);
                preparedStatement.setString(ind++, keys[i]);
                preparedStatement.setString(ind++, keys[i]);
            }

            resultSet = preparedStatement.executeQuery();
            System.out.println("Users:");
            while(resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error when searching for user: " + e.toString());
        } finally {
            closeResources();
        }
    }

    public void threeDegrees(int usera, int userb) {
		 try {
            query = "SELECT friend2, LTRIM(SYS_CONNECT_BY_PATH(friend2, '-'), '-') AS path FROM Friendships WHERE friend2 = ? START WITH friend1 = ? CONNECT BY NOCYCLE PRIOR friend2 = friend1 AND LEVEL <=3";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(2, usera);
			preparedStatement.setInt(1, userb);
            resultSet = preparedStatement.executeQuery();
			System.out.println("Paths Between " + usera + " and " + userb);
            while(resultSet.next()) {
				System.out.println("" + usera + "-" + resultSet.getString("path"));
            }
        } catch(Exception e) {
            System.out.println("There was an error finding a path between users: " + e.toString());
        } finally {
            closeResources();
        }
	}

    public boolean delGroup(int groupId) {
        boolean success = false;
        try {
            query = "DELETE FROM GROUPS WHERE GROUPID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, groupId);
            int count = preparedStatement.executeUpdate();
            System.out.println(count + " rows deleted");
            success = count > 0;
        } catch(Exception e) {
            System.out.println("Could not drop group, exception: " + e.toString());
        } finally {
            closeResources();
        }

        return success;
    }

    /*
     * Display the top k who have sent the most messages in the past x months
     */
    public void topMessagers(int k, int x) {
        try {
            query = "SELECT USERID, FNAME, MNAME, LNAME, COUNT(USERID) MSGCOUNT FROM USERS T1 JOIN MESSAGES T2 ON T1.USERID = T2.SENDER WHERE T2.DATESENT >= SYSDATE - NUMTOYMINTERVAL(?, 'MONTH') GROUP BY USERID, FNAME, MNAME, LNAME ORDER BY MSGCOUNT DESC";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, x);
            resultSet = preparedStatement.executeQuery();
            int count = 0;
            System.out.println("FNAME\tMNAME\tLNAME\tMSGCOUNT");
            while(resultSet.next() && count++ < k) {
                System.out.println(resultSet.getString(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getString(4) + "\t" + resultSet.getInt(5));
            }
        } catch(Exception e) {
            System.out.println("There was an error getting the top messengers: " + e.toString());
        } finally {
            closeResources();
        }

    }

public void dropUser(int userID) {

        try {

            query = "DELETE FROM Friendships WHERE (Friend1 = " + userID + " OR Friend2 = " + userID + ") ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);

            query = "DELETE FROM Belongs_To WHERE member = " + userID + "";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);

            statement = connection.createStatement();
            query = "ALTER TABLE Messages DROP CONSTRAINT Messaages_Sender_FK_Users";
            statement.executeQuery(query);

            query = "DELETE FROM Users WHERE userID = " + userID + "";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate(query);

            query = "UPDATE Messages SET sender = NULL WHERE sender = " + userID + "";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

            statement = connection.createStatement();
            query = "ALTER TABLE Messages ADD CONSTRAINT Messaages_Sender_FK_Users FOREIGN KEY (sender) REFERENCES Users(userID)";
            statement.executeQuery(query);
            
            query = "DELETE FROM Messages WHERE msgID NOT IN (SELECT msgID FROM Recipients) AND sender is NULL";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

            System.out.println("User drop sucessfull!");

        } catch (SQLException e) {
            System.out.println("Error:" + e);
        } finally {
            closeResources();
        }

    }

    public void automatic() {
        List<Integer> ids = new ArrayList<Integer>();
        System.out.println("Creating new users");
        for(int i=0; i<4; i++) {
            String name = "autof" + i + " autom" + i + " autol" + i;
            String email = "auto" + i + "@pitt.edu";
            String dob = "1990-01-01";
            System.out.println("Creating user(" + name + ", " + email + ", dob");
            int id = createUser(name, email, dob);
            if(id < 0) {
                System.out.println("User creation failed so cancelling automatic test");
                return;
            }
            System.out.println("User created with id of " + Integer.toString(id));

            ids.add(id);
        }

        System.out.println("Creating friendships");
        for(int i=0; i<ids.size() - 1; i++) {
            System.out.println("Init friendship bet " + ids.get(i) + " and " + ids.get(i + 1));
            initiateFriendship(ids.get(i), ids.get(i + 1));
            System.out.println("Establish friendship bet " + ids.get(i) + " and " + ids.get(i + 1));
            establishFriendship(ids.get(i), ids.get(i + 1));
        }
        System.out.println("All new friendships created");

        System.out.println("Displaying friends for each new user");
        for(int i=0; i<ids.size(); i++) {
            String name = "autof" + i + " autom" + i + " autol" + i;
            System.out.println("Friends of " + name);
            displayFriends(name);
        }
        System.out.println("Done displaying friends");

        String groupStr = "autogroup";
        System.out.println("Creating new group: " + groupStr);
        int group = createGroup(groupStr, "automatically generated group", Integer.toString(ids.size()));
        if(group < 0) {
            System.out.println("Automatic testing failed exiting");
            return;
        }

        System.out.println("Group created");
        System.out.println("Adding new users to group");
        for(Integer id : ids) {
            System.out.println("Adding user " + id + " to " + groupStr);
            addToGroup(groupStr, id);
        }
        System.out.println("All new users added");
        System.out.println("Sending new messages");
        for(int i=0; i<ids.size() - 1; i++) {
            System.out.println("Sending message from " + ids.get(i) + " to " + ids.get(i + 1));
            sendMessageToUser("subject", "body", ids.get(i + 1), ids.get(i));
        }
        System.out.println("Sending message to group " + groupStr + " with id " + group + " from  user " + ids.get(0));

        sendMessageToGroup("subject", "body", group, ids.get(0));

        System.out.println("All messages sent");
        System.out.println("Displaying all messages");
        for(Integer id : ids) {
            System.out.println("Displaying messsages for user " + id);
            displayMessages(id);
            System.out.println("Displaying new messages for user " + id);
            displayNewMessages(id);
        }
        System.out.println("All messages displayed");

        System.out.println("Searching for user with searchkey: auto");
        searchForUser("auto");
        System.out.println("Finding 3 degrees");
        for(int i=1; i<ids.size();i++) {
            System.out.println("Between user " + ids.get(0) + " and " +ids.get(i));
            threeDegrees(ids.get(0), ids.get(i));
        }

        System.out.println("Getting top 10 messagers for the past 1 month");
        topMessagers(10, 1);

        System.out.println("Dropping new users");
        for(Integer id : ids) {
            System.out.println("Dropping user " + id);
            dropUser(id);
        }
        
        System.out.println("Dropping new group");
        delGroup(group);
        System.out.println("Testing completed");
    }

    private void closeResources() {
        try {
            if(statement != null) {
                statement.close();
            }
            if(preparedStatement != null) {
                preparedStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        } catch(Exception e) {
            System.out.println("Error when closing resources: " + e.toString());
        }
    }

    private class DBMessage
    {
        public int senderID;
        public String subject;
        public String body;
        public Date dateSent;
        public int messageID;

        //Constructor
        public DBMessage(int sid, String s, String b, Date d, int mid)
        {
            senderID = sid;
            subject = s;
            body = b;
            dateSent = d;
			messageID = mid;
        }
    }

}
