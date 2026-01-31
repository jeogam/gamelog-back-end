package br.com.ifba.gamelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // Importante
import org.springframework.scheduling.annotation.Scheduled;       // Importante
import org.springframework.web.client.RestTemplate;               // Importante

@SpringBootApplication
@EnableScheduling // Habilita o agendamento de tarefas
public class GameLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameLogApplication.class, args);
    }

    // Executa a cada 14 minutos (840.000 ms) para evitar que o Render "durma"
    @Scheduled(fixedRate = 840000)
    public void keepAlive() {
        String url = "https://gamelog-back-end.onrender.com//";
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(url, String.class);
            System.out.println("Ping de manutenção enviado para: " + url);
        } catch (Exception e) {
            // Loga o erro mas não para a aplicação
            System.err.println("Erro ao tentar manter a aplicação ativa: " + e.getMessage());
        }
    }

}