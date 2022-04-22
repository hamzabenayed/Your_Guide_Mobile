package com.khedmetna.gui.back.codepromo;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.khedmetna.entities.Codepromo;
import com.khedmetna.services.CodepromoService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Manage extends Form {

    Codepromo currentCodepromo;

    Calendar calendar;
    Label codeLabel;
    TextField codeTF;

    boolean selectedStart = false;
    Date selectedStartDate = null;
    Date selectedEndDate = null;

    Button manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentCodepromo == null ? "Ajouter" : "Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentCodepromo = DisplayAll.currentCodepromo;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {
        calendar = new Calendar();
        calendar.addActionListener((l) -> {
            if (!selectedStart) {
                if (selectedStartDate != null) {
                    calendar.unHighlightDate(selectedStartDate);
                    calendar.unHighlightDate(selectedEndDate);
                }

                selectedStartDate = calendar.getDate();
                selectedEndDate = null;

                calendar.highlightDate(calendar.getDate(), "dateStart");
            } else {
                selectedEndDate = calendar.getDate();

                calendar.highlightDate(calendar.getDate(), "dateEnd");
            }

            selectedStart = !selectedStart;
        });

        codeLabel = new Label("Code : ");
        codeLabel.setUIID("labelDefault");
        codeTF = new TextField();
        codeTF.setHint("Tapez le code");

        if (currentCodepromo == null) {

            manageButton = new Button("Ajouter");
        } else {

            selectedStartDate = currentCodepromo.getDateDebut();
            selectedEndDate = currentCodepromo.getDateFin();

            calendar.highlightDate(selectedStartDate, "dateStart");
            calendar.highlightDate(selectedEndDate, "dateEnd");

            codeTF.setText(currentCodepromo.getCode());

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                calendar,
                codeLabel, codeTF,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        if (currentCodepromo == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CodepromoService.getInstance().add(
                            new Codepromo(
                                    codeTF.getText(),
                                    selectedStartDate,
                                    selectedEndDate
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Codepromo ajouté avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de codepromo. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CodepromoService.getInstance().edit(
                            new Codepromo(
                                    currentCodepromo.getId(),
                                    codeTF.getText(),
                                    selectedStartDate,
                                    selectedEndDate
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Codepromo modifié avec succes", new Command("Ok"));
                        showBackAndRefresh();
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de codepromo. Code d'erreur : " + responseCode, new Command("Ok"));
                    }
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (codeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le code", new Command("Ok"));
            return false;
        }
        if (selectedStartDate == null) {
            Dialog.show("Avertissement", "Veuillez choisir une date de debut", new Command("Ok"));
            return false;
        }
        if (selectedEndDate == null) {
            Dialog.show("Avertissement", "Veuillez choisir une date de fin", new Command("Ok"));
            return false;
        }
        if (dateIsAfter(selectedEndDate, selectedStartDate)) {
            Dialog.show("Avertissement", "Date de debut doit etre superieure a la date de fin", new Command("Ok"));
            return false;
        }

        return true;
    }

    boolean dateIsAfter(Date d1, Date d2) {

        int day1 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d1));
        int month1 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d1));
        int year1 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d1));

        int day2 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d2));
        int month2 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d2));
        int year2 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d2));

        if (year1 <= year2) {
            if (month1 <= month2) {
                return day1 <= day2;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
