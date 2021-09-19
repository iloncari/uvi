/*
 * TestView TestView.java.
 *
 */
package hr.tvz.vi.view;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.CollectionUtils;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import hr.tvz.vi.orm.Address;
import hr.tvz.vi.orm.AddressRepository;
import hr.tvz.vi.orm.City;
import hr.tvz.vi.orm.CityRepository;
import hr.tvz.vi.orm.County;
import hr.tvz.vi.orm.CountyRepository;
import hr.tvz.vi.orm.FuelConsuptionRepository;
import hr.tvz.vi.orm.Organization;
import hr.tvz.vi.orm.OrganizationRepository;
import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.PersonOrganization;
import hr.tvz.vi.orm.PersonOrganizationRepository;
import hr.tvz.vi.orm.PersonRepository;
import hr.tvz.vi.orm.ReportRepository;
import hr.tvz.vi.orm.ServiceRepository;
import hr.tvz.vi.orm.TaskRepository;
import hr.tvz.vi.orm.VechileRepository;
import hr.tvz.vi.util.Constants.Duty;
import hr.tvz.vi.util.Constants.Gender;
import hr.tvz.vi.util.Constants.OrganizationLevel;
import hr.tvz.vi.util.Constants.Professions;
import hr.tvz.vi.util.Constants.UserRole;
import hr.tvz.vi.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Route(value = "test")
@Slf4j
public class TestView extends VVerticalLayout {

  /**
	 * 
	 */
	private static final long serialVersionUID = 5494255513579362212L;
@Autowired
  FuelConsuptionRepository fuelConsuptionRepository;
  @Autowired
  ServiceRepository serviceRepository;
  @Autowired
  VechileRepository vechileRepository;
  @Autowired
  PersonRepository personRepository;
  @Autowired
  CityRepository cityRepository;
  @Autowired
  CountyRepository countyRepository;


  final List<String> oibList = new ArrayList<>();

  final Map<String, Duty> personDutyPerPersonOib = new HashMap<>();
  @Autowired
  PersonOrganizationRepository personOrganizationRepository;
  @Autowired
  OrganizationRepository organizationRepo;
  @Autowired
  TaskRepository taskRepository;
  @Autowired
  ReportRepository reportRepository;
  @Autowired
  AddressRepository addressRepository;
  
  List<City> addedCities = new ArrayList<City>();
  List<County> addedCounties = new ArrayList<County>();

  Map<String, List<Person>> personPerOrgName = new HashMap<>();

  /**
   * Instantiates a new test view.
   */
  public TestView() {
    final TextField operationTextFiedl = new TextField("operacija");
    final VButton reinitButton = new VButton("izvrsi operaciju", e -> {
      if ("show".equals(operationTextFiedl.getValue())) {
        show();
      } else if ("userForLogin".equals(operationTextFiedl.getValue())) {
        userForLogin();
      } else if ("clear".equals(operationTextFiedl.getValue())) {
        clear();
      } else if ("initRealData".equals(operationTextFiedl.getValue())) {
        initRealData();
      } else if ("test".equals(operationTextFiedl.getValue())) {
          test();
        }
      UI.getCurrent().navigate(LoginView.class);
    });
    add(operationTextFiedl);
    add(reinitButton);
  }

  /**
   * Clear.
   */
  private void clear() {
	  personOrganizationRepository.deleteAll();
    log.info("Deleting tables...");
    organizationRepo.deleteAll();
    log.info("Org deleted");
    addressRepository.deleteAll();
    log.info("Address deleted");
    cityRepository.deleteAll();
    log.info("City deleted");
    countyRepository.deleteAll();
    log.info("Couty deleted");
   
    personRepository.deleteAll();
    log.info("Person deleted");
    
    personOrganizationRepository.deleteAll();
    log.info("PersonOrganizations deleted");
    
    
    fuelConsuptionRepository.deleteAll();
    log.info("Fuel deleted");
    serviceRepository.deleteAll();
    log.info("Service deleted");
    vechileRepository.deleteAll();
    log.info("Vechile deleted");
    
    taskRepository.deleteAll();
    log.info("Task deleted");
    reportRepository.deleteAll();
    log.info("Report deleted");
    
    

   

    log.info("Tables content deleted!");

  }
  
  private List<String> getFileContent1(String fileName) {
	  String bat = "";
	  try {
		  Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		  bat = s.useDelimiter("\\Z").next();
	  }catch (Exception e) {
		// TODO: handle exception
		  log.info("error");
		  log.error(e + "");
		  return new ArrayList<String>();
	}
	  return Arrays.asList(bat.split("Next\\r\\n")).stream().filter(data -> !data.isBlank()).collect(Collectors.toList());
		
	 
  }
  
  /**
	 * Gets the file content.
	 *
	 * @param fileName the file name
	 * @return the file content
	 */
  private List<String> getFileContent(String fileName) {
	  try {
			Path path = Path.of(this.getClass().getResource("/"+fileName).toURI());
			 RandomAccessFile randomAccessFile = new RandomAccessFile(path.toFile(), "r");
		        FileChannel fileChannel = randomAccessFile.getChannel();
		        int bucketSize = 204400;
		 	   ByteBuffer byteBuffer = ByteBuffer.allocate(bucketSize);
		 	   fileChannel.read(byteBuffer);
		 	   byteBuffer.flip();
		 	  return Arrays.asList(new String(byteBuffer.array()).split("Next\\r\\n")).stream().filter(data -> !data.isBlank()).collect(Collectors.toList());
			
		} catch (Exception e) {
			log.info("Exception while trying to fetch file content " + e.getMessage());
			// TODO: handle exception
			return new ArrayList<String>();
		}

  }

  /**
   * Extract person from organization.
   *
   * @param redovi the redovi
   * @return the list
   */
  private List<Person> extractPersonFromOrganization(List<String> redovi) {
    final List<Person> members = new ArrayList<>();
    Arrays.asList("Predsjednik:", "Zapovjednik:", "Tajnik:", "Član:").forEach(titula -> {
     redovi.stream().filter(red -> red.startsWith(titula)).forEach(personRed ->{
      if (StringUtils.isNotBlank(personRed)) {
        personRed = personRed.replace(titula, "").trim();
        final List<String> personPodaciList = Arrays.asList(personRed.split(","));
        if (!personPodaciList.isEmpty()) {
          final String personNameLastName = personPodaciList.get(0).trim();
          final String name = personNameLastName.split(" ")[0];
          final String lastname = personNameLastName.split(" ")[1];
          String mob = personPodaciList.stream().filter(perPodaci -> perPodaci.trim().startsWith("mob:")).findFirst().orElse(null);
          if (StringUtils.isNotBlank(mob)) {
            mob = mob.replace("mob:", "").trim();
          }
          String personMail = personPodaciList.stream().filter(perPodaci -> perPodaci.trim().startsWith("email:")).findFirst().orElse(null);
          if (StringUtils.isNotBlank(personMail)) {
            personMail = personMail.replace("email:", "").trim();
          }
          final Person person = getExistingPersonOrNew(name, lastname);
          if (person.getIdentificationNumber() == null) {
            person.setBirthDate(genLocalDate(1950, 2000));
            person.setEmail(personMail);
            person.setGender(Gender.MALE);
            person.setIdentificationNumber(generateOIB());
            person.setLastname(lastname);
            person.setHashedPassword(BCrypt.hashpw(name.concat(lastname), BCrypt.gensalt()));
            person.setPasswordLength(name.concat(lastname).length());
            person.setName(name);
            person.setPhoneNumber(mob);
            person.setProfession(Arrays.asList(Professions.values()).get(RandomUtils.nextInt(0, Arrays.asList(Professions.values()).size() - 1)));
            person.setUsername(name.concat(lastname));
            final Duty duty = titula.equals("Predsjednik:") ? Duty.PRESIDENT : ((titula.equals("Tajnik:") ? Duty.SECRETARY : (titula.equals("Zapovjednik:") ? Duty.COMMANDER : Duty.NONE)));
            personDutyPerPersonOib.put(person.getIdentificationNumber(), duty);
          }
          if (!members.stream().anyMatch(m -> m.getName().equals(person.getName()))) {
            members.add(person);
          }
        }

      }
     });
    });

    return members;
  }
  
  private void test() {
	  getFileContent1("VZZ.txt").stream().limit(2).forEach(vzz -> {
		  final List<String> redovi = Arrays.asList(vzz.split("\\r\\n")).stream().collect(Collectors.toList());
		  log.info("site: " +redovi.size());
		  redovi.forEach(r -> log.info("*"+r+"*"));
	  });

  }

  /**
   * Generate OIB.
   *
   * @return the string
   */
  private String generateOIB() {
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    String oib = String.valueOf(random.nextLong(10_000_000_000L, 100_000_000_000L));
    while (oibList.contains(oib)) {
      oib = String.valueOf(random.nextLong(10_000_000_000L, 100_000_000_000L));
    }
    return oib;
  }

  /**
   * Gen local date.
   *
   * @param miny the miny
   * @param maxy the maxy
   * @return the local date
   */
  private LocalDate genLocalDate(int miny, int maxy) {
    final int month = RandomUtils.nextInt(1, 12);
    int day = RandomUtils.nextInt(1, 30);
    if (month == 2 && day > 28) {
      day = 20;
    }
    return LocalDate.of(RandomUtils.nextInt(miny, maxy), month, day);
  }

  private Person getExistingPersonOrNew(String name, String lastname) {
    final List<Person> lista = personPerOrgName.values().stream().filter(listaOsoba -> {
      return listaOsoba.stream().anyMatch(osoba -> osoba.getName().equals(name) && osoba.getLastname().equals(lastname));
    }).findAny().orElse(new ArrayList<Person>());
    return lista.stream().filter(osoba -> osoba.getName().equals(name) && osoba.getLastname().equals(lastname)).findAny().orElse(new Person());
  }

  /**
   * Gets the red.
   *
   * @param start the start
   * @param redovi the redovi
   * @return the red
   */
  private String getRed(String start, List<String> redovi) {
    String red = redovi.stream().filter(perPodaci -> perPodaci.startsWith(start)).findFirst().orElse(null);
    if (StringUtils.isNotBlank(red)) {
      red = red.replace(start, "").trim();
    }

    return red;
  }

  /**
   * Inits the real data.
   */
  private void initRealData() {
    clear();

    final Map<String, Organization> zajednicaZupanijeOrg = new HashMap<>();
    final Map<String, Organization> zajednicaOPcineGrada = new HashMap<>();
    final Map<String, Organization> zajednicaDVDMap = new HashMap<>();
    final Map<String, List<String>> nazivChildsVZO = new HashMap<>();
    final Map<String, List<String>> nazivChildsDVD = new HashMap<>();
    personPerOrgName.clear();

    personDutyPerPersonOib.clear();
    oibList.clear();
    
    
   addedCounties = new ArrayList<County>();
    getFileContent1("MjestaOpcineZupanije.txt").forEach(red -> {
    	List<String> redList = Arrays.asList(red.split(",")).stream().collect(Collectors.toList());
    	if(!addedCounties.stream().anyMatch(county -> county.getName().equals(redList.get(2).trim()))) {
    		County county = new County();
    		county.setName(redList.get(2).trim());
    		addedCounties.add(countyRepository.save(county));
    	}
    });
    
    addedCounties.clear();
    addedCounties.addAll(countyRepository.findAll());
    
    addedCities = new ArrayList<City>();
    getFileContent1("MjestaOpcineZupanije.txt").forEach(red -> {
    	List<String> redList = Arrays.asList(red.split(",")).stream().collect(Collectors.toList());
    	if(!addedCities.stream().anyMatch(city -> city.getName() == redList.get(0).trim() && city.getMunicipality()==redList.get(1).trim())) {
    		City city = new City();
    		city.setName(redList.get(0).trim());
    		city.setMunicipality(redList.get(1).trim());
    		city.setCounty(addedCounties.stream().filter(county -> county.getName().equals(redList.get(2).trim())).findFirst().orElse(null));
    		addedCities.add(cityRepository.save(city));
    	}
    });
    addedCities.clear();
    addedCities.addAll(cityRepository.findAll());
    
   Organization hvz = saveOrganization("Hrvatska vatrogasna zajednica", "Zagreb", "01/3025 026", "hvz@hvz.hr", "Selsa cesta", "90a", "www.hvz.gov.hr");    
    // zajednica zupanija
    getFileContent1("VZZ.txt").stream().forEach(zajednicaZupanije -> {
      final List<String> redovi = Arrays.asList(zajednicaZupanije.split("\\r\\n")).stream().collect(Collectors.toList());
      String streetNumber = null;
      String street = null;
      String city = null;

      final String naziv = redovi.get(0);
      String phone = getRed("tel:", redovi);
      if (phone != null && phone.contains(",")) {
        phone = Arrays.asList(phone.split(",")).get(0);
      }
      final String email = getRed("email:", redovi);
      final String url = getRed("url:", redovi);

      // Vatrogasna 1, 44000 Sisak
      final String adresa = redovi.stream()
        .filter(red -> red.contains(",") && (red.contains("1") || red.contains("0") || red.contains("2") || red.contains("3") || red.contains("4")
          || red.contains("5") || red.contains("6") || red.contains("7") || red.contains("8") || red.contains("9")))
        .findFirst().orElse(null);
      if (StringUtils.isNotBlank(adresa)) {
        final List<String> adressComma = Arrays.asList(adresa.split(","));
        if (!adressComma.isEmpty()) {
          final String streetAndStreetNumber = new String(adressComma.get(0)).replace("č", "c").replace("ć", "c").replace("š", "s").replace("ž", "z")
            .replace("đ", "d").replace("dž", "dz").trim();
          streetNumber = streetAndStreetNumber.replaceAll("\\b[^\\d\\W]+\\b", "").trim();
          street = streetAndStreetNumber.replaceAll("\\d+", "").trim();
          city = adressComma.get(1).trim().replaceAll("\\d+", "").trim();
        }
      }
      personPerOrgName.put(naziv, extractPersonFromOrganization(redovi));
      final Organization saveOrgZ = saveOrganization(naziv, city, phone, email, street, streetNumber, url);
      zajednicaZupanijeOrg.put(naziv, saveOrgZ);
    });
    
    if(zajednicaZupanijeOrg.isEmpty()) {
    	Utils.showErrorNotification(2000, Position.MIDDLE, "zajednicaEmpty", "VZZ");
    	return;
    }

    log.info("\n");
    log.info("___________________VZO________________");
    log.info("\n");
   getFileContent1("VZO.txt").stream().forEach(zajednicaOpcine -> {
      final List<String> redovi = Arrays.asList(zajednicaOpcine.split("\\r\\n")).stream().collect(Collectors.toList());
      String streetNumber = null;
      String street = null;
      String city = null;

      final String naziv = redovi.get(1);
      String phone = getRed("tel:", redovi);
      if (phone != null && phone.contains(",")) {
        phone = Arrays.asList(phone.split(",")).get(0);
      }
      final String email = getRed("email:", redovi);
      final String url = getRed("url:", redovi);
      final String adresa = redovi.stream()
        .filter(red -> red.contains(",") && (red.contains("1") || red.contains("0") || red.contains("2") || red.contains("3") || red.contains("4")
          || red.contains("5") || red.contains("6") || red.contains("7") || red.contains("8") || red.contains("9")))
        .findFirst().orElse(null);
      if (StringUtils.isNotBlank(adresa)) {
        final List<String> adressComma = Arrays.asList(adresa.split(","));
        if (!adressComma.isEmpty()) {
          final String streetAndStreetNumber = new String(adressComma.get(0)).replace("č", "c").replace("ć", "c").replace("š", "s").replace("ž", "z")
            .replace("đ", "d").replace("dž", "dz").trim();
          streetNumber = streetAndStreetNumber.replaceAll("\\b[^\\d\\W]+\\b", "").trim();
          street = streetAndStreetNumber.replaceAll("\\d+", "").trim();
          city = adressComma.get(1).trim().replaceAll("\\d+", "").trim();
        }
      }
      personPerOrgName.put(naziv, extractPersonFromOrganization(redovi));
      final Organization saveOrgOpc = saveOrganization(naziv, city, phone, email, street, streetNumber, url);
      
      zajednicaOPcineGrada.put(naziv, saveOrgOpc);
      final String parentNaziv = redovi.stream().filter(red -> red.contains("županije") || red.toLowerCase().equals("vatrogasna zajednica grada zagreba")).findFirst().orElse(null);

      if (StringUtils.isNotBlank(parentNaziv)) {
        if (nazivChildsVZO.containsKey(parentNaziv)) {
          nazivChildsVZO.get(parentNaziv).add(saveOrgOpc.getName());
        } else {
          final List<String> l1 = new ArrayList<>();
          l1.add(saveOrgOpc.getName());
          nazivChildsVZO.put(parentNaziv, l1);
        }
      }
    });
    
    if(zajednicaOPcineGrada.isEmpty()) {
    	Utils.showErrorNotification(2000, Position.MIDDLE, "zajednicaEmpty", "VZO");
    	return;
    }
   

    final Map<String, List<String>> opcinaGradovi = new HashMap<>();
  getFileContent1("MjestaOpcine.txt").forEach(gO -> {
      final String gradOpcina = gO.replace("\\r\\n", "").trim();
      final List<String> gradOpcinaList = Arrays.asList(gradOpcina.split(","));
      if (opcinaGradovi.containsKey(gradOpcinaList.get(1).toLowerCase())) {
        opcinaGradovi.get(gradOpcinaList.get(1).toLowerCase()).add(gradOpcinaList.get(0).trim().toLowerCase());
      } else {
        final List<String> gradovu = new ArrayList<>();
        gradovu.add(gradOpcinaList.get(0).trim().toLowerCase());
        opcinaGradovi.put(gradOpcinaList.get(1).trim().toLowerCase(), gradovu);
      }
    });
    
    if(opcinaGradovi.isEmpty()) {
    	Utils.showErrorNotification(2000, Position.MIDDLE, "zajednicaEmpty", "Opcina Grad");
    	return;
    }
    

    log.info("\n");
    log.info("___________________DVD________________");
    log.info("\n");

   getFileContent1("DVD.txt").stream().forEach(zajednicaDVD -> {
      final List<String> redovi = Arrays.asList(zajednicaDVD.split("\\r\\n")).stream().collect(Collectors.toList());
      String streetNumber = null;
      String street = null;
      String city = null;

      final String naziv = redovi.stream().filter(red -> red.toLowerCase().startsWith("dobrovoljno vatrogasno")).findFirst().orElse("");
      String phone = getRed("tel:", redovi);
      if (phone != null && phone.contains(",")) {
        phone = Arrays.asList(phone.split(",")).get(0);
      }
      final String email = getRed("email:", redovi);
      final String url = getRed("url:", redovi);
      final String adresa = redovi.stream()
        .filter(red -> red.contains(",") && (red.contains("1") || red.contains("0") || red.contains("2") || red.contains("3") || red.contains("4")
          || red.contains("5") || red.contains("6") || red.contains("7") || red.contains("8") || red.contains("9")))
        .findFirst().orElse(null);
      if (StringUtils.isNotBlank(adresa)) {
        final List<String> adressComma = Arrays.asList(adresa.split(","));
        if (!adressComma.isEmpty()) {
          final String streetAndStreetNumber = new String(adressComma.get(0)).replace("č", "c").replace("ć", "c").replace("š", "s").replace("ž", "z")
            .replace("đ", "d").replace("dž", "dz").trim();
          streetNumber = streetAndStreetNumber.replaceAll("\\b[^\\d\\W]+\\b", "").trim();
          street = streetAndStreetNumber.replaceAll("\\d+", "").trim();
          city = adressComma.get(1).trim().replaceAll("\\d+", "").trim();
        }
      }
      personPerOrgName.put(naziv, extractPersonFromOrganization(redovi));
      final Organization saveOrgOpc = saveOrganization(naziv, city, phone, email, street, streetNumber, url);

      zajednicaDVDMap.put(naziv, saveOrgOpc);

      final String parent = redovi.get(0);
      final List<String> vzos = nazivChildsVZO.get(parent.trim());

      String opcina = "";
      for (final Entry<String, List<String>> opcinaGrads : opcinaGradovi.entrySet()) {
        final String mjestoNaziv = naziv.toLowerCase().replace("dobrovoljno vatrogasno društvo ", "").trim();
        if (opcinaGrads.getValue().stream().anyMatch(naselje -> mjestoNaziv.equals(naselje))) {
          opcina = opcinaGrads.getKey();
        }
      }

      if (StringUtils.isNotBlank(opcina) && !CollectionUtils.isEmpty(vzos)) {
        final String opcinaGrad = opcina;
        final Optional<String> matchedVZO = vzos.stream().filter(vzo -> !vzo.startsWith("Javna vatrogasna postrojba")).filter(vzo -> {
          if (vzo.toLowerCase().startsWith("vatrogasna zajednica grada ")) {
            final String vzo1 = vzo.toLowerCase().replace("vatrogasna zajednica grada ", "");
            return opcinaGrad.contains(vzo1.substring(0, vzo1.length() - 2));
          } else if (vzo.toLowerCase().startsWith("vatrogasna zajednica općine ")) {
            return vzo.toLowerCase().contains(opcinaGrad);
          }
          return false;
        }).findFirst();
        if (matchedVZO.isPresent()) {
          if (nazivChildsDVD.get(matchedVZO.get()) != null) {
            nazivChildsDVD.get(matchedVZO.get()).add(naziv);
          } else {
            final List<String> nazivs = new ArrayList<>();
            nazivs.add(naziv);
            nazivChildsDVD.put(matchedVZO.get(), nazivs);
          }

        }
      }
    });
    
    if(zajednicaDVDMap.isEmpty()) {
    	Utils.showErrorNotification(2000, Position.MIDDLE, "zajednicaEmpty", "DVD");
    	return;
    }
    
    
log.info("_______________________PARENT DVD____________________");
    nazivChildsDVD.forEach((vzo, childs) -> {
      final Organization parent = zajednicaOPcineGrada.get(vzo);
      if (parent == null) {
        return;
      }
      final Set<Organization> childOrgs = new HashSet<>();
      childs.forEach(child -> {
        final Organization org = zajednicaDVDMap.get(child);
        if (org != null) {
          childOrgs.add(org);
        }
      });
      parent.setChilds(childOrgs);
   
   
      organizationRepo.save(parent);
  
    });

    log.info("_______________________PARENT VZO____________________");
    nazivChildsVZO.forEach((vzz, childs) -> {
      final Organization parent = zajednicaZupanijeOrg.get(vzz);
      if (parent == null) {
        return;
      }
      final Set<Organization> childOrgs = new HashSet<>();
      childs.forEach(child -> {
        final Organization org = zajednicaOPcineGrada.get(child);
        if (org != null) {
          childOrgs.add(org);
        }
      });
      parent.setChilds(childOrgs);
      organizationRepo.save(parent);
    });
    
    log.info("_______________________PARENT HVZ____________________");
    hvz.setChilds(zajednicaZupanijeOrg.values().stream().collect(Collectors.toSet()));
    organizationRepo.save(hvz);
    Person hvzMember = new Person();
    hvzMember.setBirthDate(genLocalDate(1980, 1990));
    hvzMember.setEmail("slavko.tucakovic@hvz.hr");
    hvzMember.setGender(Gender.MALE);
    hvzMember.setHashedPassword(BCrypt.hashpw("SlavkoTucaković", BCrypt.gensalt()));
    hvzMember.setPasswordLength("SlavkoTucaković".length());
    hvzMember.setIdentificationNumber(generateOIB());
    hvzMember.setLastname("Tucaković");
    hvzMember.setName("Slavko");
    hvzMember.setPhoneNumber("016475647");
    hvzMember.setProfession(Professions.FIRST_CLASS_OFFICER);
    hvzMember.setUsername("SlavkoTucaković");
    
    personPerOrgName.put("Hrvatska vatrogasna zajednica", Arrays.asList(hvzMember));
    personDutyPerPersonOib.put(hvzMember.getIdentificationNumber(), Duty.PRESIDENT);
    
    log.info("_______________________MEMBERS____________________");
    final List<Organization> organizacije = organizationRepo.findAll();
    personPerOrgName.forEach((naziv, clanovi) -> {
      final Organization org = organizacije.stream().filter(o -> o.getName().equals(naziv)).findFirst().orElse(null);
      if (org == null) {
        return;
      }
      clanovi.forEach(clan -> {
        final Person p = personRepository.save(clan);
        final PersonOrganization pooo = savePersonOrganization(p, org);
      });
    });

    Utils.showSuccessNotification(2000, Position.TOP_CENTER, "zajednicaNotEmpty");
  }

  /**
   * Save organization.
   *
   * @param name the name
   * @param city the city
   * @param phone the phone
   * @param email the email
   * @param street the street
   * @param streetN the street N
   * @param url the url
   * @return the organization
   */
  private Organization saveOrganization(String name, String city, String phone, String email, String street, String streetN,
    String url) {
    final String oib = generateOIB();
    final Organization organization = new Organization();
    
    City addressCity = addedCities.stream().filter(c -> c.getName().equals(city)).findFirst().orElse(null);
   if(addressCity != null) {
	   Address address = new  Address();
	   address.setCity(addressCity);
	   address.setStreet(street);
	   address.setStreetNumber(streetN);
	//   Address savedAddress = addressRepository.save(address);
	 //  organization.setAddress(savedAddress);
   }
 
    organization.setContactNumber(phone);
    organization.setEmail(email);
    organization.setIban("HR" + oib);
    organization.setIdentificationNumber(oib);
    organization.setName(name);
    organization.setEstablishmentDate(genLocalDate(1890, 2020));
    organization.setUrl(url);
    if(name.equals("Hrvatska vatrogasna zajednica")) {
    	organization.setLevel(OrganizationLevel.COUNTRY_LEVEL);
    }else if(name.toLowerCase().contains("županije") || name.toLowerCase().contains("vatrogasna zajednica grada zagreba")) {
    	organization.setLevel(OrganizationLevel.REGIONAL_LEVEL);
    }else if(name.toLowerCase().contains("vatrogasna zajednica grada") || name.toLowerCase().contains("vatrogasna zajednica općine")) {
    	organization.setLevel(OrganizationLevel.CITY_LEVEL);
    }else {
    	organization.setLevel(OrganizationLevel.OPERATIONAL_LEVEL);
    }
    return organizationRepo.save(organization);
  }

  /**
   * Save person organization.
   *
   * @param person the person
   * @param organization the organization
   * @return the person organization
   */
  private PersonOrganization savePersonOrganization(Person person, Organization organization) {
    final PersonOrganization po = new PersonOrganization();
    po.setAppRights(true);
    po.setDuty(personDutyPerPersonOib.getOrDefault(person.getIdentificationNumber(), Duty.NONE));
    po.setJoinDate(genLocalDate(1956, 2020));
    po.setOrganization(organization);
    po.setPerson(person);
    po.setRole(UserRole.MANAGER);
    return personOrganizationRepository.save(po);
  }

  /**
   * Show.
   */
  private void show() {

    final List<Person> personList = personRepository.findAll();
    personList.forEach(person -> {
      if (person.getOrgList().isEmpty()) {
        return;
      }
      log.info("Osoba" + (person.getUsername() != null ? "*" : "") + ": " + person.getId() + " " + person.getName());
      person.getOrgList().forEach(po -> {
        log.info("   " + (po.getExitDate() == null ? "*" : "") + po.getId() + " (" + po.getOrganization().getId() + "/"
          + po.getOrganization().getName().replace(" ", "") + ")" + " " + po.getOrganization().getParentId());
      });
    });
  }

  /**
   * User for login.
   */
  private void userForLogin() {
    personRepository.findAll().stream().filter(person -> {
      if (StringUtils.isAnyBlank(person.getUsername(), person.getHashedPassword())) {
        return false;
      }
      return person.getOrgList().stream().anyMatch(po -> po.getExitDate() == null && po.getOrganization().getParentId() != null);

    }).findAny().ifPresent(p -> Notification.show(p.getUsername() + " " + p.getHashedPassword()));
  }
}
