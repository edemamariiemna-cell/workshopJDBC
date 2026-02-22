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

            switch(choix){
                case 1:
                    System.out.print("User ID: "); int uid = sc.nextInt();
                    System.out.print("Calories: "); int cal = sc.nextInt();
                    System.out.print("Proteines: "); int prot = sc.nextInt();
                    System.out.print("Glucides: "); int carb = sc.nextInt();
                    System.out.print("Lipides: "); int fat = sc.nextInt(); sc.nextLine();
                    System.out.print("Goal type: "); String goal = sc.nextLine();
                    NutritionPlan plan = new NutritionPlan(0, uid, cal, prot, carb, fat, goal);
                    planService.ajouter(plan);
                    System.out.println("NutritionPlan ajouté avec ID: " + plan.getId());
                    break;

                case 2:
                    System.out.print("ID plan à modifier: "); int pidM = sc.nextInt(); sc.nextLine();
                    NutritionPlan planM = planService.getPlanById(pidM);
                    if(planM!=null){
                        System.out.print("Nouvelles calories: "); planM.setCalorieTarget(sc.nextInt());
                        System.out.print("Nouvelles proteines: "); planM.setProteinTarget(sc.nextInt());
                        System.out.print("Nouveaux glucides: "); planM.setCarbTarget(sc.nextInt());
                        System.out.print("Nouveaux lipides: "); planM.setFatTarget(sc.nextInt()); sc.nextLine();
                        System.out.print("Nouveau goal: "); planM.setGoalType(sc.nextLine());
                        planService.modifier(planM);
                        System.out.println("Plan modifié !");
                    } else System.out.println("Plan introuvable !");
                    break;

                case 3:
                    System.out.print("ID plan à supprimer: "); int pidDel = sc.nextInt();
                    planService.supprimer(pidDel);
                    System.out.println("Plan supprimé !");
                    break;

                case 4:
                    System.out.print("Plan ID: "); int planId = sc.nextInt(); sc.nextLine();
                    NutritionPlan p = planService.getPlanById(planId);
                    if(p==null){ System.out.println("Plan introuvable, créez-le d'abord !"); break; }
                    System.out.print("Date (YYYY-MM-DD): "); String date = sc.nextLine();
                    System.out.print("Meal type: "); String type = sc.nextLine();
                    System.out.print("Calories: "); int cals = sc.nextInt();
                    System.out.print("Proteines: "); int prots = sc.nextInt();
                    System.out.print("Glucides: "); int carbs = sc.nextInt();
                    System.out.print("Lipides: "); int fats = sc.nextInt();
                    System.out.print("Health score: "); int score = sc.nextInt(); sc.nextLine();
                    System.out.print("Notes: "); String notes = sc.nextLine();
                    mealService.ajouter(new DailyMealLog(planId, date, type, cals, prots, carbs, fats, score, notes));
                    System.out.println("Meal ajouté !");
                    break;

                case 5:
                    System.out.print("ID meal à modifier: "); int mid = sc.nextInt();
                    System.out.print("Plan ID du meal: "); int planIdMeal = sc.nextInt(); sc.nextLine();
                    List<DailyMealLog> mealsM = mealService.getMealsByPlan(planIdMeal);
                    DailyMealLog mealModif = mealsM.stream().filter(m -> m.getId()==mid).findFirst().orElse(null);
                    if(mealModif!=null){
                        System.out.print("Nouvelle calories: "); mealModif.setCalories(sc.nextInt());
                        mealService.modifier(mealModif);
                        System.out.println("Meal modifié !");
                    } else System.out.println("Meal introuvable !");
                    break;

                case 6:
                    System.out.print("ID meal à supprimer: "); int midDel = sc.nextInt();
                    mealService.supprimer(midDel);
                    System.out.println("Meal supprimé !");
                    break;

                case 7:
                    System.out.print("ID plan à afficher: "); int pid = sc.nextInt();
                    NutritionPlan loadedPlan = planService.getPlanWithMeals(pid);
                    if(loadedPlan!=null){
                        System.out.println("\n" + loadedPlan);
                        System.out.println("Meals associés:");
                        for(DailyMealLog m: loadedPlan.getMeals()) System.out.println(m);
                    } else System.out.println("Plan introuvable !");
                    break;

                case 0: System.out.println("Au revoir !"); break;
                default: System.out.println("Choix invalide !"); break;
            }
        } while(choix!=0);

        sc.close();
    }
}
