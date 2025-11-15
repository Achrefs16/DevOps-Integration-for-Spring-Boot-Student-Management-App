# Role: java-install

## Description
Ce rôle Ansible installe Java JDK sur les systèmes Debian/Ubuntu.

## Variables
- `java_version`: Version de Java à installer (défaut: 11)

## Exemple d'utilisation
```yaml
- hosts: servers
  roles:
    - role: java-install
      vars:
        java_version: 17
```

## Prérequis
- Système d'exploitation: Ubuntu/Debian
- Privilèges sudo
