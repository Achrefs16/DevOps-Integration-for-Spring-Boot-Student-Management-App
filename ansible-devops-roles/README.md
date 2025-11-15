# Ansible DevOps Roles

## Description
Collection de rôles Ansible pour automatiser l'installation des outils DevOps essentiels.

## Rôles disponibles
- **java-install**: Installation de Java JDK
- **docker-install**: Installation et configuration de Docker
- **kubectl-install**: Installation de kubectl
- **jenkins-install**: Installation et configuration de Jenkins

## Structure du projet
```
ansible-devops-roles/
├── roles/
│   ├── java-install/
│   ├── docker-install/
│   ├── kubectl-install/
│   └── jenkins-install/
├── playbook.yml
├── inventory.ini
└── README.md
```

## Utilisation

### Installation
```bash
git clone <votre-repo>
cd ansible-devops-roles
```

### Exécution
```bash
ansible-playbook -i inventory.ini playbook.yml
```

## Prérequis
- Ansible >= 2.9
- Ubuntu/Debian
- Privilèges sudo

## Auteur
Achref Saidi
