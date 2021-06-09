<?xml version="1.0" encoding="UTF-8"?>
<model ref="r:5e5d25ff-7a94-408f-989c-7cf0a44bc9d6(EcoreLanguage.sanbox)">
  <persistence version="9" />
  <languages>
    <use id="45e9c502-be8d-4b95-92c9-8ad2f7c494aa" name="EcoreLanguage" version="1" />
  </languages>
  <imports />
  <registry>
    <language id="45e9c502-be8d-4b95-92c9-8ad2f7c494aa" name="EcoreLanguage">
      <concept id="4431084978597324274" name="EcoreLanguage.structure.ETypedElement" flags="ng" index="2rCCmX">
        <property id="6361799312994550580" name="upperBound" index="2f5enK" />
        <property id="6361799312994550576" name="lowerBound" index="2f5enO" />
      </concept>
      <concept id="166962917938782486" name="EcoreLanguage.structure.EReference" flags="ng" index="2$d$FP">
        <reference id="166962917938782489" name="eType" index="2$d$FU" />
      </concept>
      <concept id="5921274573544793991" name="EcoreLanguage.structure.EClass" flags="ng" index="1BB31t">
        <child id="166962917938779138" name="eReferences" index="2$dBZx" />
        <child id="5921274573544823634" name="eAttribute" index="1BBoM8" />
      </concept>
      <concept id="5921274573544806062" name="EcoreLanguage.structure.EDataType" flags="ng" index="1BB45O" />
      <concept id="5921274573544802721" name="EcoreLanguage.structure.EEnum" flags="ng" index="1BB5TV">
        <child id="5921274573544802722" name="eLiterals" index="1BB5TS" />
      </concept>
      <concept id="5921274573544831328" name="EcoreLanguage.structure.EEnumLiteral" flags="ng" index="1BBqUU" />
      <concept id="5921274573544826994" name="EcoreLanguage.structure.EAttribute" flags="ng" index="1BBrYC">
        <reference id="5921274573544826999" name="eType" index="1BBrYH" />
      </concept>
      <concept id="1577982146714689164" name="EcoreLanguage.structure.EPackage" flags="ng" index="3Lc43E">
        <property id="1577982146714689166" name="nsPrefix" index="3Lc43C" />
        <property id="1577982146714689165" name="nsURI" index="3Lc43F" />
        <child id="1577982146714689170" name="eClassifiers" index="3Lc43O" />
      </concept>
      <concept id="1577982146714732451" name="EcoreLanguage.structure.EFactory" flags="ng" index="3LchB5">
        <child id="5921274573544746159" name="ePackage" index="1BwRHP" />
      </concept>
    </language>
    <language id="ceab5195-25ea-4f22-9b92-103b95ca8c0c" name="jetbrains.mps.lang.core">
      <concept id="1133920641626" name="jetbrains.mps.lang.core.structure.BaseConcept" flags="ng" index="2VYdi">
        <property id="1193676396447" name="virtualPackage" index="3GE5qa" />
      </concept>
      <concept id="1169194658468" name="jetbrains.mps.lang.core.structure.INamedConcept" flags="ng" index="TrEIO">
        <property id="1169194664001" name="name" index="TrG5h" />
      </concept>
    </language>
  </registry>
  <node concept="3LchB5" id="5x9DGjnmRkU">
    <node concept="3Lc43E" id="5x9DGjnmRkV" role="1BwRHP">
      <property role="TrG5h" value="familymodel" />
      <property role="3Lc43F" value="http://www.example.org/familymodel" />
      <property role="3Lc43C" value="familymodel" />
      <node concept="1BB5TV" id="5qTU7U3AdSP" role="3Lc43O">
        <property role="TrG5h" value="Gender" />
        <node concept="1BBqUU" id="1zSZfBshcjb" role="1BB5TS">
          <property role="TrG5h" value="Female" />
        </node>
      </node>
      <node concept="1BB31t" id="5qTU7U3AdTr" role="3Lc43O">
        <property role="TrG5h" value="Family" />
        <node concept="2$d$FP" id="5qTU7U3AdTB" role="2$dBZx">
          <property role="2f5enO" value="0" />
          <property role="2f5enK" value="-1" />
          <property role="TrG5h" value="Persons" />
          <ref role="2$d$FU" node="5x9DGjnn3$Q" resolve="Person" />
        </node>
      </node>
      <node concept="1BB31t" id="5x9DGjnn3$Q" role="3Lc43O">
        <property role="TrG5h" value="Person" />
        <node concept="2$d$FP" id="5x9DGjnnkcC" role="2$dBZx">
          <property role="TrG5h" value="children" />
          <property role="2f5enO" value="0" />
          <property role="2f5enK" value="-1" />
          <ref role="2$d$FU" node="5x9DGjnn3$Q" resolve="Person" />
        </node>
        <node concept="1BBrYC" id="5qTU7U3AeKq" role="1BBoM8">
          <property role="2f5enO" value="0" />
          <property role="2f5enK" value="1" />
          <property role="TrG5h" value="name" />
          <ref role="1BBrYH" node="5x9DGjnnjmJ" resolve="EString" />
        </node>
        <node concept="1BBrYC" id="5x9DGjnnkcd" role="1BBoM8">
          <property role="TrG5h" value="age" />
          <ref role="1BBrYH" node="5x9DGjnnjmH" resolve="EInt" />
        </node>
        <node concept="1BBrYC" id="5qTU7U3AdTF" role="1BBoM8">
          <property role="2f5enO" value="0" />
          <property role="2f5enK" value="1" />
          <property role="TrG5h" value="gender" />
          <ref role="1BBrYH" node="5qTU7U3AdSP" resolve="Gender" />
        </node>
        <node concept="2$d$FP" id="5x9DGjnnkcA" role="2$dBZx">
          <property role="TrG5h" value="partner" />
          <property role="2f5enO" value="0" />
          <property role="2f5enK" value="1" />
          <ref role="2$d$FU" node="5x9DGjnn3$Q" resolve="Person" />
        </node>
      </node>
    </node>
  </node>
  <node concept="1BB45O" id="5x9DGjnnhH4">
    <property role="TrG5h" value="EBooleanBObject" />
    <property role="3GE5qa" value="datatypes" />
  </node>
  <node concept="1BB45O" id="5x9DGjnnjmG">
    <property role="3GE5qa" value="datatypes" />
    <property role="TrG5h" value="EBoolean" />
  </node>
  <node concept="1BB45O" id="5x9DGjnnjmH">
    <property role="3GE5qa" value="datatypes" />
    <property role="TrG5h" value="EInt" />
  </node>
  <node concept="1BB45O" id="5x9DGjnnjmI">
    <property role="3GE5qa" value="datatypes" />
    <property role="TrG5h" value="EDouble" />
  </node>
  <node concept="1BB45O" id="5x9DGjnnjmJ">
    <property role="3GE5qa" value="datatypes" />
    <property role="TrG5h" value="EString" />
  </node>
</model>

