class CreateRentals < ActiveRecord::Migration[5.0]
  def change
    create_table :rentals do |t|
      t.string :renter
      t.date :checkedout
      t.date :returned
      t.string :email
      t.string :phone
      t.integer :locknum
      t.boolean :helmet
      t.integer :amountpaid
      t.references :bicycle, foreign_key: true

      t.timestamps
    end
  end
end
