package com.wakatime.android.test.support;

import com.wakatime.android.dashboard.model.Editor;
import com.wakatime.android.dashboard.model.Language;
import com.wakatime.android.dashboard.model.OperatingSystem;
import com.wakatime.android.dashboard.model.Project;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.model.Wrapper;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;

/**
 * @author Joao Pedro Evangelista
 */

@SuppressWarnings("WeakerAccess")
public class FakeSupport {

    public static String fakeAuthKey() {
        return "sec_123456";
    }

    public static Stats fakeSevenDaysStats() {
        Stats stats = new Stats();
        stats.setStart(new Date());
        stats.setAlreadyUpdating(false);
        stats.setCreatedAt(new Date());
        stats.setDailyAverage(4000);
        stats.setDaysIncludingHolidays(4);
        stats.setDaysMinusHoliday(2);
        stats.setUpToDate(true);
        stats.setEditors(fakeEditors());
        stats.setEnd(new Date());
        stats.setHolidays(2);
        stats.setHumanReadableDailyAverage("8 hours 0 minutes");
        stats.setHumanReadableTotal("40 hours 0 minutes");
        stats.setId(UUID.randomUUID().toString());
        stats.setLanguages(fakeLanguages());
        stats.setModifiedAt(new Date());
        stats.setOperatingSystems(fakeOperatingSystems());
        stats.setProjects(fakeProjects());
        stats.setRange("last_7_days");
        stats.setStatus("ok");
        stats.setStuck(false);
        stats.setTimeout(15);
        stats.setTimezone("America/Sao_Paulo");
        stats.setTotalSeconds(123456);
        return stats;
    }

    public static RealmList<Editor> fakeEditors() {
        Editor editor = new Editor();
        editor.setTotalSeconds(3600);
        editor.setDigital("01:00");
        editor.setHours(1);
        editor.setMinutes(0);
        editor.setName("AndroidStudio");
        editor.setPercent(100.0f);
        editor.setText("01 hour 00 minutes");
        return new RealmList<>(editor);

    }

    public static RealmList<Project> fakeProjects() {
        Project project = new Project();
        project.setTotalSeconds(3600);
        project.setDigital("01:00");
        project.setHours(1);
        project.setMinutes(0);
        project.setName("Tick");
        project.setPercent(100.0f);
        project.setText("01 hour 00 minutes");
        return new RealmList<>(project);
    }

    public static RealmList<OperatingSystem> fakeOperatingSystems() {
        OperatingSystem operatingSystem = new OperatingSystem();
        operatingSystem.setTotalSeconds(3600);
        operatingSystem.setDigital("01:00");
        operatingSystem.setHours(1);
        operatingSystem.setMinutes(0);
        operatingSystem.setName("AndroidStudio");
        operatingSystem.setPercent(100.0f);
        operatingSystem.setText("01 hour 00 minutes");
        return new RealmList<>(operatingSystem);
    }

    public static RealmList<Language> fakeLanguages() {
        Language language = new Language();
        language.setTotalSeconds(3600);
        language.setDigital("01:00");
        language.setHours(1);
        language.setMinutes(0);
        language.setName("Java");
        language.setPercent(100.0f);
        language.setText("01 hour 00 minutes");
        return new RealmList<>(language);
    }

    public static Wrapper<Stats> wrapper(Stats data) {
        Wrapper<Stats> wrapper = new Wrapper<>();
        wrapper.setData(data);
        return wrapper;
    }


}
