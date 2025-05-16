package bt.nhdcl.maintenancemicroservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Preventive maintenance
                        .requestMatchers(HttpMethod.GET, "/api/maintenance").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/maintenance").hasAuthority("Manager")
                        .requestMatchers(HttpMethod.GET, "/api/maintenance/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/maintenance/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/maintenance/{id}").hasAuthority("Manager")
                        .requestMatchers(HttpMethod.GET, "/api/maintenance/status/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/maintenance/date/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/maintenance/asset/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/maintenance/user/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/maintenance/technician/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/maintenance/send-email").permitAll()

                        // === PreventiveMaintenanceReportController ===
                        .requestMatchers(HttpMethod.POST, "/api/maintenance-reports/start-time")
                        .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/maintenance-reports/{maintenanceReportID}/end-time")
                        .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/maintenance-reports/complete/{maintenanceReportID}")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/maintenance-reports").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/maintenance-reports").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/maintenance-reports/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/maintenance-reports/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/maintenance-reports/{id}")
                        .hasAnyAuthority("Admin", "Manager")
                        .requestMatchers(HttpMethod.GET, "/api/maintenance-reports/by-maintenance-id/**")
                        .permitAll()

                        // Repair
                        .requestMatchers(HttpMethod.POST, "/api/repairs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/repairs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/repairs/{repairID}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/repairs/update/{repairID}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/repairs/{repairID}").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.PUT, "/api/repairs/{repairId}/accept").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/repairs/schedule/{repairId}").permitAll()

                        // Schedule
                        .requestMatchers(HttpMethod.POST, "/api/schedules").hasAnyAuthority("Manager", "Supervisor")
                        .requestMatchers(HttpMethod.GET, "/api/schedules").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/schedules/{scheduleID}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/schedules/user/{userID}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/schedules/repair/{repairID}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/schedules/{scheduleID}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/schedules/{scheduleID}").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.GET, "/api/schedules/technician/{email}").permitAll()

                        // Repair Report
                        .requestMatchers(HttpMethod.POST, "/api/repair-reports/start-time").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/repair-reports/{reportID}/end-time").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/repair-reports/complete/{reportID}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/repair-reports").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/repair-reports/{repairReportID}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/repair-reports/repair/{repairID}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/repair-reports/{repairReportID}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/repair-reports/{repairReportID}").authenticated()

                        //Graph
                        .requestMatchers(HttpMethod.GET, "/api/cost").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/analytics").permitAll()

                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
