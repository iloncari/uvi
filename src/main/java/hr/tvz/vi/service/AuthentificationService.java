/*
 * AuthentificationService AuthentificationService.java.
 *
 */
package hr.tvz.vi.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import hr.tvz.vi.orm.Person;
import hr.tvz.vi.orm.PersonRepository;

/**
 * The Class AuthentificationService.
 *
 * @author Igor Lončarić (iloncari2@tvz.hr)
 * @since 8:17:25 PM Aug 10, 2021
 */
@Service
public class AuthentificationService extends AbstractService<Person> {

  /**
   * Login.
   *
   * @param username the username
   * @param password the password
   * @return the person
   */
  public Person login(String username, String password) {
    if (StringUtils.isAnyBlank(username, password)) {
      return null;
    }

    final Optional<Person> person = ((PersonRepository)repository).findByUsername(username);
    if (person.isEmpty()) {
      return null;
    }

    if (BCrypt.checkpw(password, person.get().getHashedPassword())) {
      return person.get();
    }
    return null;
  }

}
