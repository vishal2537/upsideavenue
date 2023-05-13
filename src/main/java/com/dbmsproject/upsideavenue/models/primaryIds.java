package com.dbmsproject.upsideavenue.models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class primaryIds implements Serializable {

  @OneToOne
  @JoinColumn(name = "postId", nullable = false)
  private Post postId;

  @OneToOne
  @JoinColumn(name = "sellerId", nullable = false)
  private User sellerId;

  @OneToOne
  @JoinColumn(name = "buyerId", nullable = false)
  private User buyerId;
}
