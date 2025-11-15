# Role: docker-install

## Description
Ce rôle Ansible installe et configure Docker CE sur Ubuntu/Debian.

## Fonctionnalités
- Installation de Docker CE
- Configuration du service Docker
- Ajout de l'utilisateur au groupe docker

## Exemple d'utilisation
```yaml
- hosts: servers
  roles:
    - docker-install
```

## Prérequis
- Système d'exploitation: Ubuntu/Debian
- Privilèges sudo
