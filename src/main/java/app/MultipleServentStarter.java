package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultipleServentStarter {

    private static class ServentCLI implements Runnable {
        private List<Process> serventProcesses;
        private Process bsProcess;
        public ServentCLI(List<Process> serventProcesses, Process bsProcess) {
            this.serventProcesses = serventProcesses;
            this.bsProcess = bsProcess;
        }
        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);
            while(true) {
                String line = sc.nextLine();

                if(line.equals("stop")) {
                    for(Process process : serventProcesses) {
                        process.destroy();
                    }
                    bsProcess.destroy();
                    break;
                }
            }
            sc.close();
        }
    }

    private static void startServentTest(String testName) {
        List<Process> serventProcesses = new ArrayList<>();

        AppConfig.readConfig(testName+"/servent_list.properties", 0);

        AppConfig.timestampedStandardPrint("Starting multiple servent runner. "
                + "If servents do not finish on their own, type \"stop\" to finish them");

        Process bsProcess = null;
        ProcessBuilder bsBuilder = new ProcessBuilder("java", "-cp", "out\\production\\kids_pr_jelena_simic_rn2720", "app.BootstrapServer", String.valueOf(AppConfig.BOOTSTRAP_PORT));
        try {
            bsProcess = bsBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int serventCount = AppConfig.SERVENT_COUNT;

        for (int i = 0; i < serventCount; i++) {
            try {
                ProcessBuilder builder = new ProcessBuilder("java", "-cp", "out\\production\\kids_pr_jelena_simic_rn2720", "app.ServentMain",
                        testName+"/servent_list.properties", String.valueOf(i));

                builder.redirectOutput(new File(testName+"/output/servent"+i+"_out.txt"));
                builder.redirectOutput(new File(testName+"/error/servent"+i+"_err.txt"));
                builder.redirectOutput(new File(testName+"/input/servent"+i+"_in.txt"));

                Process p = builder.start();
                serventProcesses.add(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try { // 10s for each node !!! should be changed !
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread t = new Thread(new ServentCLI(serventProcesses, bsProcess));
        t.start(); // waiting for "stop"

        for (Process p : serventProcesses) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        AppConfig.timestampedStandardPrint("All servent processes finished. Type \"stop\" to halt bootstrap.");
        try {
            bsProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        startServentTest("kademlia");
        startServentTest("chord"); // chord mozda cu ja nesto drugo da radim ?
    }
}
