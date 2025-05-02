package com.act.material.repository;

import com.act.material.model.Certificate;
import com.act.material.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

}
