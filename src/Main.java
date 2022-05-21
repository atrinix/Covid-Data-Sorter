/**
 *   FileName: Main.java
 * Assignment: CSC-220 Final
 *       Name: Arielle Riray
 *    SFSU ID: 917861209
 *       Date: 12-15-2021
 */

import java.io.FileReader;
import java.io.*;
import java.math.*;
import java.lang.*;
import java.util.* ;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {

    private static final Map<String, String> continentMap = new HashMap<>();
    private static final Map<String, Integer> populationMap = new HashMap<>();
    private static final Map<String, Integer> covidCasesMap = new HashMap<>();
    private static final Map<String, Integer> covidDeathsMap = new HashMap<>();

    static void loadData() {

        String pathFile1 = "full_data.csv";
        String pathFile2 = "locations.csv";
        int cases, deaths;
        String row;

        /**
         * Loads in data from full_data.cvs into two hashmaps,
         * one represents covidCasesMap, and the other is covidDeathsMap
         */
        try {
            BufferedReader loadCSVintoMap = new BufferedReader(new FileReader(pathFile1));

            loadCSVintoMap.readLine();
            while ((row = loadCSVintoMap.readLine()) != null) {
                String[] tokenizer = row.split(",");
                String country = tokenizer[1];

                cases = Integer.parseInt(tokenizer[4]);
                deaths = Integer.parseInt(tokenizer[5]);

                covidCasesMap.put(country, cases);
                covidDeathsMap.put(country, deaths);
            }
            loadCSVintoMap.close();

            /**
             * This next map Loader has a bunch of conditional statements
             * due to the inconsistencies in the locations.csv file
             */

            loadCSVintoMap = new BufferedReader(new FileReader(pathFile2));
            loadCSVintoMap.readLine();

            while ((row = loadCSVintoMap.readLine()) != null) {
                String[] tokenizer = row.split(",");
                String continent = "none";
                String country2;
                int pop;

                //Separate if-statement to check for missing comma values
                if (tokenizer.length == 2) {
                    continent = " ";
                        //System.out.println("HIT");
                } else if (tokenizer.length == 5) {
                    continent = tokenizer[2];
                }
                        //System.out.print(continent + " ");

                //Second set of if-statements to put into hashmaps
                if (tokenizer.length < 4) {
                    country2 = (tokenizer[1]);
                    pop = -1;
                        //System.out.println("No continent detected");
                    //continentMap.put(country2, continent);
                    populationMap.put(country2, pop);

                } else if (tokenizer.length == 6) {
                    continent = tokenizer[3];
                    country2 = (tokenizer[2]);
                    pop = Integer.parseInt(tokenizer[5]);
                    continentMap.put(country2, continent);
                    populationMap.put(country2, pop);

                } else {
                    country2 = (tokenizer[1]);
                    pop = Integer.parseInt(tokenizer[4]);
                    continentMap.put(country2, continent);
                    populationMap.put(country2, pop);
                }
                    //System.out.println(country2);
            }
            loadCSVintoMap.close();


            /**
             * Catch try block in case for errors
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Main driver method with the menu for the user to use
     * Uses Base case to cycle though menu options
     *
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int choice, userChoice;
        boolean lowOrHigh;

        System.out.println();
        System.out.println("*************************************************************");
        System.out.println("****COVID 19 Global Statistics Menu (Last Update May 3rd)****");
        System.out.println("*************************************************************");

        do {
            System.out.println("\n[1] Load Data From Files");
            System.out.println("[2] Print Continents Total Cases (Lowest to Highest)");
            System.out.println("[3] Print Continents Total Cases (Highest to Lowest)");
            System.out.println("[4] Print Continents Total Deaths (Lowest to Highest)");
            System.out.println("[5] Print Continents Total Deaths (Highest to Lowest)");
            System.out.println("[6] Prioritize top countries for testing based on new cases per 1 million");
            System.out.println("[7] To Exit");
            System.out.print("Please enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\nLoading files");
                    loadData();
                    System.out.println("Files loaded!");
                    break;
                case 2:
                    lowOrHigh = false;
                    System.out.println(" \nNow displaying Continents Total Cases (Lowest to Highest)");
                    System.out.println("\nContinent                             Total Cases");
                    printByContinent(covidCasesMap, continentMap, lowOrHigh);
                    break;
                case 3:
                    lowOrHigh = true;
                    System.out.println(" \nNow displaying Continents Total Cases (Highest to Lowest)");
                    System.out.println("\nContinent                             Total Cases");
                    printByContinent(covidCasesMap, continentMap, lowOrHigh);
                    break;
                case 4:
                    lowOrHigh = false;
                    System.out.println(" \nNow displaying Continents Total Deaths (Lowest to Highest)");
                    System.out.println("\nContinent                             Total Deaths");
                    printByContinent(covidDeathsMap, continentMap, lowOrHigh);
                    break;
                case 5:
                    lowOrHigh = true;
                    System.out.println(" \nNow displaying Continents Total Deaths (Highest to Lowest)");
                    System.out.println("\nContinent                             Total Deaths");
                    printByContinent(covidDeathsMap, continentMap, lowOrHigh);
                    break;
                case 6:
                    System.out.println("\nHow many countries to poll from the priority list? ");
                    userChoice = scanner.nextInt();
                    System.out.println("\nContinent                      Total Cases Per Million");
                    sortByPriority(covidCasesMap, populationMap, userChoice);
                    break;
                case 7:
                    System.out.println("\nGoodbye!");
                    break;

                    /**
                     * Hidden case which I used for debugging purposes, came very much in handy
                case 8:
                    System.out.println("covidDeathsMap:");
                    System.out.println(covidDeathsMap);
                    System.out.println("covidCasesMap:");
                    System.out.println(covidCasesMap);
                    System.out.println("continentMap:");
                    System.out.println(continentMap);
                    System.out.println("PopulationMap:");
                    System.out.println(populationMap);
                    break; */
                default:
                    System.out.print("Invalid input. Please enter choice 1 - 7: ");
                    break;
            }
        } while (choice != 7);
    }


    /**
     * This method takes in two hashmaps and combines it into one hashmap.
     * That hashmap is then converted into a List, which can be sorted.
     * This can take in multiple arguments, and uses conditionals on how
     * to order the sort depending on the user's menu choice.
     */
    public static void printByContinent(Map<String, Integer> casesDeathsMap, Map<String, String> continentsMap, Boolean lowHigh) {

            //This loads the two hashmaps (of different data types) into a new third hashmap using a Collectors function I found when I was practicing HashMaps last assignment
        Map<String, Integer> MergedMap = continentsMap.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.summingInt(e -> casesDeathsMap.get(e.getKey()))));

            //Initializes the third hashmap into an array
        List<Map.Entry<String, Integer>> MergedMapList = new ArrayList<>(MergedMap.entrySet());

            //uses boolean conditional to determine how the list would be sorted
        if (!lowHigh) {
            MergedMapList.sort(Map.Entry.comparingByValue());
        } else {
            MergedMapList.sort((num1, num2) -> num2.getValue().compareTo(num1.getValue()));
        }

            //Prints out the newly sorted list

        int listCounter = 1;
        for (Map.Entry<String, Integer> pair : MergedMapList) {
            System.out.printf(listCounter + ". %-35s%-35d\n", pair.getKey(), pair.getValue());
            listCounter++;
        }
    }

    /**
     * This method is used to find Covid Cases per 1 million
     * Two Hashmaps would be loaded into one hashmap,
     * where cases would be divided by population, and
     * displayed from Highest to Lowest order.
     */
    public static void sortByPriority(Map<String, Integer> covidDeaths, Map<String, Integer> population, int choice) {

            //The 'World' line was causing issues in the code, and I didn't need it to display anyway, so I removed it
        covidDeaths.remove("World");

            //Initialize 3rd HasMap
        HashMap<String, BigDecimal> MergedMap = new HashMap<>();
        BigDecimal convert, t, d;

        /**
         * For Loop to merge the two hashmaps into one hashmap whilst
         * dividing the cases by population.
         *
         * There's a lot of commented out print statements here
         * I ued them for debugging purposes, it came very
         * much in handy.
         */

        for (Map.Entry<String, Integer> entry : covidDeaths.entrySet()) {
            Integer t1 = entry.getValue();
                //System.out.print(t1);
            Integer d1 = population.get(entry.getKey());
                //System.out.print(" " + d1);
            t = new BigDecimal(t1);
            d = new BigDecimal(d1);
            convert = t.divide(d, 5, RoundingMode.CEILING); //Referenced from document in Final Instructions
            MergedMap.put(entry.getKey(), convert); }
                //System.out.print(" " +  convert + " -> ");}
                //System.out.println("MergedMap size: " + MergedMap.size());
                //System.out.println("MergedMap size: " + MergedMap);

            //Initialize a List
        List<Map.Entry<String, BigDecimal>> MergedMapList = new ArrayList<>(MergedMap.entrySet());

            //Using a sort method, but reversed to get Highest to Lowest
        MergedMapList.sort((num1, num2) -> num2.getValue().compareTo(num1.getValue()));

            //Setting index and counter variables
        int listCounter = 1, index = 0;
            //While Loop to print out the results, thankfully it matches the PDF results
        while (index < choice && index < MergedMapList.size()) {
            System.out.printf(listCounter + ". %-35s%.5f\n", MergedMapList.get(index).getKey(), MergedMapList.get(index).getValue());
            index ++;
            listCounter++;
        }
    }

}


