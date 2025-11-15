# Role: kubectl-install

## Description
Ce rôle Ansible installe l'outil en ligne de commande kubectl pour Kubernetes.

## Variables
- `kubectl_version`: Version de kubectl à installer (défaut: v1.28.0)

## Exemple d'utilisation
```yaml
- hosts: servers
  roles:
    - role: kubectl-install
      vars:
        kubectl_version: v1.29.0
```

## Prérequis
- Accès Internet
- Privilèges sudo
