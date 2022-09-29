import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

// 2020313582 - Selin Samra

public class Blackjack {

    public static void main(String[] args) {
        int seed = Integer.parseInt(args[0]);

        Deck deck = new Deck(); //Create the deck
        deck.shuffle(seed); //Shuffle the deck
        int numPlayer = Integer.parseInt(args[1]);

        Player player = new Player();
        House dealer = new House();
        Computer[] players = new Computer[numPlayer+1]; //create the players
        for (int i =2;i<=numPlayer;i++) {
            players[i] = new Computer();
        }
        //first cards
        player.addCards(deck);
        for (int i =2;i<=numPlayer;i++) {
            players[i].addCards(deck);
        }
        dealer.addCards(deck);
        //second cards
        player.addCards(deck);
        for (int i =2;i<=numPlayer;i++) {
            players[i].addCards(deck);
        }
        dealer.addCards(deck);

        dealer.firstRound().printCard();

        player.getProperties();
        for (int i =2;i<=numPlayer;i++) {
            players[i].getProperties(i);
        }
        //check dealer
        dealer.check21();
        if (dealer.twentyOne == true) {
            System.out.print("\n\n---Game Results---");
            dealer.getProperties();
            player.lose();
            for (int i =2;i<=numPlayer;i++) {
                players[i].lose(i);
            }
        }
        else {
            player.myTurn(deck);
            for (int i =2;i<=numPlayer;i++)
            {
                players[i].myTurn(deck,i);
            }
            dealer.myTurn(deck);

            System.out.print("\n\n---Game Results---");
            dealer.getProperties();
            player.getResults(dealer.getSum());

            for (int i =2;i<=numPlayer;i++) {
                players[i].getResults(i,dealer.getSum());
            }
        }
    }
}


class Card{
    private int value;
    private int suit;
    public Card() {}
    public Card(int theValue, int theSuit) {
        this.value=theValue;
        this.suit=theSuit;
    }
    String[] suits = {"c","h","d","s"};
    String[] values = {null, "A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    public String translateCards() {
        return values[this.value] + suits[this.suit];
    }
    public int getValue() {
        return value;
    }
    public int getSuit() {
        return suit;
    }

    public void printCard() {
        System.out.print(translateCards());
    }
}

class Deck{
    private Card[] deck;
    private int cardsUsed;
    public int numCards = 52;
    public Deck() {
        int d= 0;
        this.deck = new Card[this.numCards];
        for(int i=1;i<=13;) {
            for(int j=0;j<4;j++) {
                this.deck[d] = new Card(i,j);
                d++;
            }
            i++;
        }
    }
    public void shuffle(int seed) {
        Random random = new Random(seed);
        for(int i = deck.length-1; i>0; i--) {
            int rand = (int)(random.nextInt(i+1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }
    public Card dealCard() {
        if (cardsUsed == deck.length)
            throw new IllegalStateException("No cards are left  in the deck.");
        cardsUsed++;
        return deck[cardsUsed - 1];
    }
    
    public Card getCard() {
        return dealCard();
    }
}

class Hand{ //set of cards in your hand
    protected ArrayList<Card> hand;
    public Hand() {
        this.hand = new ArrayList<>();
    }
    public Hand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    public ArrayList<Card> getCards(){
        return hand;
    }
    public void addCards(Deck deck) {
        hand.add(deck.dealCard());
    }
    public Card getTheSecondOne() {
        return hand.get(1);
    }
    public Card getTheCards(int i) {
        return hand.get(i);
    }
    public int cnt =0;
    public int aceNumber() {
        int counter = 0;
        for(Card x : hand) {
            if(x.getValue() == 1) {
                counter++;
            }
        }
        return counter;
    }
    public int getSum() {
        int sum = 0;
        for (Card x : hand) {
            sum += x.getValue();
            if(x.getValue()==1) {
            	sum+=10;
            }
            else if (x.getValue() == 11) {
                sum -= 1;
            }
            else if (x.getValue() == 12) {
                sum -= 2;
            }
            else if (x.getValue() == 13) {
                sum -= 3;
            }
          
        }
        if (sum > 21 && aceNumber() != 0) {
            int decrease = aceNumber() * 10;
            sum -= decrease;
            }
        
        return sum;
    }
}
    class Computer extends Hand{ //player automatically participates
        public Computer() {super();};
        public Computer[] player;
        int n;
        public void myTurn(Deck deck, int i) {
            System.out.print("\n\n---Player" + i + "Turn---");
            getProperties(i);
            while(getSum()<=21)
            {
                if(n==1) break;
                addOrNot(deck,i);
            }
        }

        public void lose(int i) {
            System.out.print("\n[Lose]Player" + i + ": ");
            getTheList();
            System.out.print(" " + "(" + getSum() + ")");
        }
        public void getResults(int i,int dealer) {
            if(getSum()>21)
            {
                System.out.print("\n[Lose]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")"+ " - Bust!");
            }
            else if (getSum()==21 && dealer<21) {
                System.out.print("\n[Win]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
            else if (getSum() > dealer && getSum()<21) {
                System.out.print("\n[Win]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" );
            }
            else if(getSum()==21 && dealer>21) {
            	System.out.print("\n[Win]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" );
            }
            else if(getSum() == dealer) {
                System.out.print("\n[Draw]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
            else if(dealer>21 && getSum()<21) {
                System.out.print("\n[Win]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");}
            else {
                System.out.print("\n[Lose]Player"+i+": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }

        }

        public void getProperties(int i) {
            if (getSum()>21) {
                System.out.print("\nPlayer" + i + ": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" + " - Bust!");
            }
            else {
                System.out.print("\nPlayer" + i + ": ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
        }
        int sum;
        public int getSum() {
            sum = super.getSum();
            return sum;
        }
        public void getTheList() {
            for (int i = 0;i<hand.size();i++) {
                super.getTheCards(i).printCard();
                System.out.print(", ");
            }
        }
        public void addOrNot(Deck deck, int i) {
            if (sum < 14) {
                System.out.print("\nHit");
                super.addCards(deck);
                cnt++;
                getProperties(i);
            }
            if (sum > 17) {
                System.out.print("\nStand");
                super.getCards();
                getProperties(i);
                n=1;
            }
            if(sum>=14 && sum<=17) {
                Random random = new Random();
                int is_hit = (int)(random.nextInt(2));
                if (is_hit == 1) {
                    System.out.print("\nHit");
                    super.addCards(deck);
                    cnt++;
                    getProperties(i);
                }
                else if (is_hit == 0) {
                    System.out.print("\nStand");
                    super.getCards();
                    getProperties(i);
                    n=1;
                }
            }

        }

    }

    class Player extends Hand{  //player you control
        int sum;

        public Player() {super();};
        public void myTurn(Deck deck) {
            System.out.print("\n\n---Player1 Turn---");
            getProperties();
            System.out.println("\n");
            addOrNot(deck);
        }

        public void lose() {
            System.out.print("\n[Lose]Player1: ");
            getTheList();
            System.out.print(" " + "(" + getSum() + ")");
        }
        public void getResults(int dealer) {
            if(getSum()>21)
            {
                System.out.print("\n[Lose]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")"+ " - Bust!");
            }
            else if (getSum()==21 && dealer<21) {
                System.out.print("\n[Win]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
            else if (getSum() > dealer && getSum()<21) {
                System.out.print("\n[Win]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" );
            }
            else if(getSum() == dealer) {
                System.out.print("\n[Draw]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
            else if(dealer>21 && getSum()<21)
            {
                System.out.print("\n[Win]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
            else if(getSum()<21 && dealer>getSum()) {
            	System.out.print("\n[Lose]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
            else {
                System.out.print("\n[Lose]Player1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" + "Bust!");
            }
        }

        public void getProperties() {
            getSum();
            if (getSum()>21) {
                System.out.print("\nPlayer1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" + " - Bust!");

            }
            else {
                System.out.print("\nPlayer1: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
        }
        public void getTheList() {
            for (int i = 0;i<hand.size();i++) {
                super.getTheCards(i).printCard();
                System.out.print(", ");
            }
        }

        public int getSum() {
            sum = super.getSum();
            return sum;}
        public boolean ask = true;
        public void addOrNot(Deck deck) {
            
            Scanner scan = new Scanner(System.in);
            while(ask == true) {
                String answer = scan.nextLine();
                if(answer.equalsIgnoreCase("Hit")) {
                    super.addCards(deck);
                    getProperties();
                    System.out.print("\n");
                    cnt++;
                    if(getSum()>21) {
                    	ask=false;
                    }
                    else
                    	ask = true;
                }
                else if (answer.equalsIgnoreCase("Stand")) {
                    super.getCards();
                    getProperties();
                    ask = false;
                }
                else {
                	ask = false;
                }
            }
        }
    }
    class House extends Hand{
        public House() {super();};
        public int sum;
        int n=0;
        public void myTurn(Deck deck) {
            System.out.print("\n\n---House Turn---");
            getProperties();
            addOrNot(deck);
            while(getSum()<=21)
            {
                if(n==1) break;
                addOrNot(deck);
            }
        }
        public boolean twentyOne = false;
        public void check21() {
            if (getSum()==21) {
                twentyOne = true;
            }
            else
                twentyOne = false;
        }
        public void getProperties() {
            if (getSum()>21) {
                System.out.print("\nHouse: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")" + " - Bust!");
            }
            else {
                System.out.print("\nHouse: ");
                getTheList();
                System.out.print(" " + "(" + getSum() + ")");
            }
        }
        public int getSum() {
            sum = super.getSum();
            return sum;
        }
        public void getTheList() {
            for (int i = 0;i<hand.size();i++) {
                super.getTheCards(i).printCard();
                System.out.print(", ");
            }
        }

        public void addOrNot(Deck deck) {
            if(getSum() <= 16) {
                System.out.print("\nHit");
                super.addCards(deck);
                cnt++;
                getProperties();
            }
            else if (getSum() > 17) {
                System.out.print("\nStand");
                super.getCards();
                getProperties();
                n=1;
            }
        }
        public Card firstRound() {
            super.getCards();
            System.out.print("House: HIDDEN, ");
            return super.getTheSecondOne();
        }
    }