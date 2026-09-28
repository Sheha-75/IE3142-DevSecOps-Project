/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3"
})
public class InsecureDeserializationTask implements AssignmentEndpoint {

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String token) throws IOException {
    String b64token = token.replace('-', '+').replace('_', '/');

    try (ObjectInputStream ois =
        new ObjectInputStream(
            new ByteArrayInputStream(Base64.getDecoder().decode(b64token)))) {

      /*
       * Restrict deserialization to the exact classes required by this
       * lesson. The filter is installed before readObject() is called.
       */
      ois.setObjectInputFilter(
          info -> {
            Class<?> clazz = info.serialClass();

            /*
             * Some serialization metadata does not provide a class.
             * Leave those decisions to the default serialization mechanism.
             */
            if (clazz == null) {
              return ObjectInputFilter.Status.UNDECIDED;
            }

            String className = clazz.getName();

            /*
             * Allow only the classes required for the legitimate lesson
             * object graph.
             */
            if (className.equals(VulnerableTaskHolder.class.getName())
                || className.equals("java.lang.String")
                || className.equals("java.time.LocalDateTime")
                || className.equals("java.time.Ser")) {

              return ObjectInputFilter.Status.ALLOWED;
            }

            /*
             * Reject every other serialized class.
             */
            return ObjectInputFilter.Status.REJECTED;
          });

      long before = System.currentTimeMillis();

      Object deserializedObject = ois.readObject();

      /*
       * Defense in depth: even an allowed class must still be the
       * expected lesson object.
       */
      if (!(deserializedObject instanceof VulnerableTaskHolder)) {
        if (deserializedObject instanceof String) {
          return failed(this)
              .feedback("insecure-deserialization.stringobject")
              .build();
        }

        return failed(this)
            .feedback("insecure-deserialization.wrongobject")
            .build();
      }

      long after = System.currentTimeMillis();
      int delay = (int) (after - before);

      /*
       * The original lesson expected an OS-level delay caused by the
       * vulnerable readObject() implementation.
       *
       * After remediation, no external process is executed, so the
       * deserialization should complete quickly and the old vulnerable
       * behavior must not be considered successful.
       */
      if (delay > 7000 || delay < 3000) {
        return failed(this).build();
      }

      return success(this).build();

    } catch (InvalidClassException e) {
      /*
       * ObjectInputFilter rejection reaches this path.
       */
      return failed(this)
          .feedback("insecure-deserialization.invalidversion")
          .build();

    } catch (IllegalArgumentException e) {
      /*
       * Invalid Base64 or an expired task is rejected.
       */
      return failed(this)
          .feedback("insecure-deserialization.expired")
          .build();

    } catch (Exception e) {
      /*
       * Fail securely for unexpected deserialization errors.
       */
      return failed(this)
          .feedback("insecure-deserialization.invalidversion")
          .build();
    }
  }
}