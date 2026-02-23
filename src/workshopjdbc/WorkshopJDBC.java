package workshopjdbc;

import tn.edu.esprit.entities.NutritionPlan;
import tn.edu.esprit.entities.DailyMealLog;
import tn.edu.esprit.services.ServiceNutritionPlan;
import tn.edu.esprit.services.ServiceDailyMealLog;

import java.util.List;
import java.util.Scanner;

public class WorkshopJDBC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ServiceNutritionPlan planService = new ServiceNutritionPlan();
        ServiceDailyMealLog mealService = new ServiceDailyMealLog();

        int choix;
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Ajouter un NutritionPlan");
            System.out.println("2. Modifier un NutritionPlan");
            System.out.println("3. Supprimer un NutritionPlan");
            System.out.println("4. Ajouter un DailyMealLog");
            System.out.println("5. Modifier un DailyMealLog");
            System.out.println("6. Supprimer un DailyMealLog");
            System.out.println("7. Afficher NutritionPlan + Meals");
            System.out.println("0. Quitter");
            System.out.print("Choix: ");
            choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {

                case 1:
                    System.out.print("Calories: ");
                    int cal = sc.nextInt();
                    System.out.print("Proteines: ");
                    int prot = sc.nextInt();
                    System.out.print("Glucides: ");
                    int carb = sc.nextInt();
                    System.out.print("Lipides: ");
                    int fat = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Goal type: ");
                    String goal = sc.nextLine();

                    NutritionPlan plan = new NutritionPlan(0, cal, prot, carb, fat, goal);
                    planService.ajouter(plan);
                    break;

                case 2:
                    System.out.print("ID plan à modifier: ");
                    int pidM = sc.nextInt();
                    sc.nextLine();

                    NutritionPlan planM = planService.getOne(pidM);

                    if (planM != null) {
                        System.out.print("Nouvelles calories: ");
                        planM.setCalorieTarget(sc.nextInt());
                        System.out.print("Nouvelles proteines: ");
                        planM.setProteinTarget(sc.nextInt());
                        System.out.print("Nouveaux glucides: ");
                        planM.setCarbTarget(sc.nextInt());
                        System.out.print("Nouveaux lipides: ");
                        planM.setFatTarget(sc.nextInt());
                        sc.nextLine();
                        System.out.print("Nouveau goal: ");
                        planM.setGoalType(sc.nextLine());

                        planService.modifier(planM);
                    } else {
                        System.out.println("Plan introuvable !");
                    }
                    break;

                case 3:
                    System.out.print("ID plan à supprimer: ");
                    int pidDel = sc.nextInt();
                    planService.supprimer(pidDel);
                    break;

                case 4:
                    System.out.print("Plan ID: ");
                    int planId = sc.nextInt();
                    sc.nextLine();

                    NutritionPlan p = planService.getOne(planId);

                    if (p == null) {
                        System.out.println("Plan introuvable !");
                        break;
                    }

                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = sc.nextLine();
                    System.out.print("Meal type: ");
                    String type = sc.nextLine();
                    System.out.print("Calories: ");
                    int cals = sc.nextInt();
                    System.out.print("Proteines: ");
                    int prots = sc.nextInt();
                    System.out.print("Glucides: ");
                    int carbs = sc.nextInt();
                    System.out.print("Lipides: ");
                    int fats = sc.nextInt();
                    System.out.print("Health score: ");
                    int score = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Notes: ");
                    String notes = sc.nextLine();

                    DailyMealLog meal = new DailyMealLog(planId, date, type,
                            cals, prots, carbs, fats, score, notes);

                    mealService.ajouter(meal);
                    break;

                case 5:
                    System.out.print("ID meal à modifier: ");
                    int mid = sc.nextInt();
                    sc.nextLine();

                    DailyMealLog mealModif = mealService.getOne(mid);

                    if (mealModif != null) {
                        System.out.print("Nouvelle calories: ");
                        mealModif.setCalories(sc.nextInt());
                        sc.nextLine();

                        mealService.modifier(mealModif);
                    } else {
                        System.out.println("Meal introuvable !");
                    }
                    break;

                case 6:
                    System.out.print("ID meal à supprimer: ");
                    int midDel = sc.nextInt();
                    mealService.supprimer(midDel);
                    break;

                case 7:
                    System.out.print("ID plan à afficher: ");
                    int pid = sc.nextInt();

                    NutritionPlan loadedPlan = planService.getOne(pid);

                    if (loadedPlan != null) {
                        System.out.println("\n" + loadedPlan);
                        System.out.println("Meals associés:");

                        List<DailyMealLog> meals = mealService.getByPlan(pid);
                        for (DailyMealLog m : meals) {
                            System.out.println(m);
                        }
                    } else {
                        System.out.println("Plan introuvable !");
                    }
                    break;

                case 0:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide !");
            }

        } while (choix != 0);

        sc.close();
    }
}