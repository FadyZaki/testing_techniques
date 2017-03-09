package ins;

public class Insurance {
   /**
    * Calculate the premium for car insurance.
    * 
    * @param age     The age of the applicant (must be positive)
    * @param gender  The gender of the applicant ('M' or 'F')
    * @param married Whether the applicant is married
    * 
    * @return
    * 0 - uninsurable age or illegal inputs<br>
    * 200 - married or female, and 45<=age<=65<br>
    * 300 - married or female, and 16<=age<45<br>
    * 400 - single male and 45<=age<=65<br>
    * 500 - single male and 25<=age<45<br>
    * 2000 - single male and age<25
    */
	public static int premium(int age, char gender, boolean married) {
		int premium;
		if ((age<16) || (age>65) || (gender!='M' && gender!='F')) {
			premium=0;
		} else {
			premium=500;
			if ((age<25) && (gender=='M') && (!married)) {
				premium += 1500;
			} else {
				if (married || gender=='F') 
					premium -= 200;
				if ((age>=45) && (age<=65)) 
					premium -= 100;
			}
		}
		return premium;
	}
}
