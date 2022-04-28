import java.time.LocalDateTime;

public class Test{
    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("Start time: " + startTime);
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Current time: " + currentTime);
        int startDay = startTime.getDayOfYear();
        int currentDay = currentTime.getDayOfYear();
        if((startDay >= 365) && currentDay == 1){
            System.out.println("it's a new year");
        }
        else if(currentDay > startDay){
            System.out.println("it's a new day");
        }
        else{
            System.out.println("it's the same day");
        }
    }
}