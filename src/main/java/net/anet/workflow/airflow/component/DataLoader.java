package net.anet.workflow.airflow.component;

import net.anet.workflow.airflow.domain.WrkAnonType;
import net.anet.workflow.airflow.repository.WrkAnonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final Logger log = LoggerFactory.getLogger(DataLoader.class);
    @Autowired
    private WrkAnonTypeRepository anonRepo;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        long rows = anonRepo.count();
        if(rows > 0){
            log.info("WrkAnonTypeRepository is up to date");
        }else{
            log.info("Loading data into WrkAnonTypeRepository");
            List<WrkAnonType> anonList = new ArrayList<>();
            anonList.add(getWrkAnonType("DUTCHBANKACCOUNT","Generates valid Dutch Bank Account number with the same length as the given" +
                " number"));
            anonList.add(getWrkAnonType("IBAN","Generates valid International Bank Account Numbers "));
            anonList.add(getWrkAnonType("RANDOMCHARACTERS","CharacterStringAnonymizer Generates an output string based on the configured characters to use"));
            anonList.add(getWrkAnonType("PREFETCHCHARACTERS","CharacterStringPrefetchAnonymizer Provides the same functionality as CharacterStringAnonymizer}, but" +
                " uses the prefetch cycle"));
            anonList.add(getWrkAnonType("COUNTRY_CODE","CountryCodeAnonymizer Produces valid 2 or 3 character country codes"));
            anonList.add(getWrkAnonType("DATE","Date Anonymizer"));
            anonList.add(getWrkAnonType("RANDOMDIGITS","DigitStringAnonymizer"));
            anonList.add(getWrkAnonType("EMAIL_ADDRESS","EmailAddressAnonymizer"));
            anonList.add(getWrkAnonType("ELVEN_NAME","ElvenNameGenerator"));
            anonList.add(getWrkAnonType("ROMAN_NAME","RomanNameGenerator"));
            anonList.add(getWrkAnonType("STRING","StringAnonymizer"));
            anonList.add(getWrkAnonType("UUID","UUIDAnonymizer"));

            anonRepo.saveAll(anonList);
        }
    }

    private WrkAnonType getWrkAnonType(String name, String descr){
        WrkAnonType at1 = new WrkAnonType();
        at1.setName(name);
        at1.setDescription(descr);
        return at1;
    }
}
