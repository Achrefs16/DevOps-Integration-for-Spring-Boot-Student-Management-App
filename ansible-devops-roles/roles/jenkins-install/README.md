# Role: jenkins-install

## Description
Ce rôle Ansible installe et configure Jenkins sur Ubuntu/Debian.

## Variables
- `jenkins_port`: Port d'écoute de Jenkins (défaut: 8080)

## Exemple d'utilisation
```yaml
- hosts: servers
  roles:
    - role: jenkins-install
      vars:
        jenkins_port: 8080
```

## Post-installation
Accédez à Jenkins via: http://votre-serveur:8080
Le mot de passe initial sera affiché dans les logs Ansible.

## Prérequis
- Java doit être installé (utilisez le rôle java-install)
- Privilèges sudo
